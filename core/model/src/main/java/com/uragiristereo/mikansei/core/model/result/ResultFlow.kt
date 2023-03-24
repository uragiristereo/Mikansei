package com.uragiristereo.mikansei.core.model.result

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
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
                val data = response.body()!!

                emit(Result.Success(data))
            }

            else -> emit(Result.Failed(message = failedResponseFormatter(response)))
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
