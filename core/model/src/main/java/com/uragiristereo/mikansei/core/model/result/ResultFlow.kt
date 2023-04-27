package com.uragiristereo.mikansei.core.model.result

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

inline fun <reified A> resultFlow(
    crossinline block: suspend () -> Response<A>,
): Flow<Result<A>> = flow {
    try {
        val response = block()

        when {
            response.isSuccessful -> {
                when {
                    A::class == Unit::class -> emit(Result.Success(Unit as A))
                    else -> {
                        val data = response.body()!!

                        emit(Result.Success(data))
                    }
                }
            }

            else -> emit(
                Result.Failed(
                    message = failedResponseFormatter(
                        responseCode = response.code(),
                        errorBody = response.errorBody()?.string(),
                    )
                )
            )
        }
    } catch (t: Throwable) {
        when (t) {
            is CancellationException -> {}
            else -> emit(Result.Error(t))
        }
    }
}

suspend inline fun <reified A, reified B> Flow<Result<A>>.mapSuccess(
    crossinline block: suspend (A) -> B,
): Flow<Result<B>> = map { result ->
    when (result) {
        is Result.Success -> Result.Success(block(result.data))
        is Result.Failed -> Result.Failed(result.message)
        is Result.Error -> Result.Error(result.t)
    }
}

inline fun <reified T1, reified T2> Flow<Result<T1>>.combineResultFlow(
    resultFlow: Flow<Result<T2>>,
): Flow<Result<Pair<T1, T2>>> {
    return combine(resultFlow) { result1, result2 ->
        when {
            result1 is Result.Success && result2 is Result.Success -> Result.Success(Pair(result1.data, result2.data))
            result1 is Result.Failed -> Result.Failed(result1.message)
            result2 is Result.Failed -> Result.Failed(result2.message)
            result1 is Result.Error -> Result.Error(result1.t)
            result2 is Result.Error -> Result.Error(result2.t)

            else -> throw IllegalStateException("Unexpected result types")
        }
    }
}

inline fun <reified T1, reified T2, reified R> Flow<Result<T1>>.combineSuccess(
    resultFlow: Flow<Result<T2>>,
    crossinline transform: suspend (result1: T1, result2: T2) -> R,
): Flow<Result<R>> {
    return combine(resultFlow) { result1, result2 ->
        when {
            result1 is Result.Success && result2 is Result.Success -> Result.Success(transform(result1.data, result2.data))
            result1 is Result.Failed -> Result.Failed(result1.message)
            result2 is Result.Failed -> Result.Failed(result2.message)
            result1 is Result.Error -> Result.Error(result1.t)
            result2 is Result.Error -> Result.Error(result2.t)

            else -> throw IllegalStateException("Unexpected result types")
        }
    }
}
