package com.uragiristereo.mikansei.core.model.result

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import retrofit2.Response

sealed interface Result<T> {
    data class Success<T>(val data: T) : Result<T>

    data class Failed<T>(val message: String) : Result<T>

    data class Error<T>(val t: Throwable) : Result<T>
}

suspend inline fun <reified T> resultOf(
    crossinline block: suspend () -> Response<T>,
): Result<T> {
    try {
        val response = block()

        if (response.isSuccessful) {
            val isClassUnit = T::class == Unit::class

            return when {
                isClassUnit -> Result.Success(Unit as T)
                else -> {
                    val data = response.body()!!

                    Result.Success(data)
                }
            }
        } else {
            return Result.Failed(
                message = failedResponseFormatter(
                    responseCode = response.code(),
                    errorBody = response.errorBody()?.string(),
                )
            )
        }
    } catch (t: Throwable) {
        return Result.Error(t)
    }
}

suspend inline fun <reified T1, reified T2> Result<T1>.mapSuccess(
    crossinline block: suspend (T1) -> T2,
): Result<T2> {
    return when (this) {
        is Result.Success -> Result.Success(block(data))
        is Result.Failed -> Result.Failed(message)
        is Result.Error -> Result.Error(t)
    }
}

suspend inline fun <reified T1, reified T2, reified R> combineSuccess(
    crossinline first: suspend () -> Result<T1>,
    crossinline other: suspend () -> Result<T2>,
    crossinline transform: suspend (result1: T1, result2: T2) -> R,
): Result<R> {
    return coroutineScope {
        val results = awaitAll(
            async { first() },
            async { other() },
        )

        @Suppress("UNCHECKED_CAST")
        val firstResult = results[0] as Result<T1>

        @Suppress("UNCHECKED_CAST")
        val otherResult = results[1] as Result<T2>

        when {
            firstResult is Result.Success && otherResult is Result.Success -> {
                Result.Success(transform(firstResult.data, otherResult.data))
            }

            firstResult is Result.Failed -> Result.Failed(firstResult.message)
            otherResult is Result.Failed -> Result.Failed(otherResult.message)
            firstResult is Result.Error -> Result.Error(firstResult.t)
            otherResult is Result.Error -> Result.Error(otherResult.t)

            else -> throw IllegalStateException("Unexpected result types")
        }
    }
}
