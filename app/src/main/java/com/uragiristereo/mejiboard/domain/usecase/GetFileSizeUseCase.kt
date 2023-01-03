package com.uragiristereo.mejiboard.domain.usecase

import com.uragiristereo.mejiboard.data.network.NetworkRepository
import kotlinx.coroutines.CancellationException

class GetFileSizeUseCase(
    private val networkRepository: NetworkRepository,
) {
    suspend operator fun invoke(
        url: String,
        onLoading: (loading: Boolean) -> Unit,
        onSuccess: (size: Long) -> Unit,
        onFailed: (message: String) -> Unit,
        onError: (t: Throwable) -> Unit,
    ) {
        onLoading(true)

        try {
            val response = networkRepository.api.checkFile(url)

            when {
                response.isSuccessful -> {
                    val size = response.headers()["content-length"]?.toLongOrNull() ?: 0L

                    onSuccess(size)
                }

                else -> onFailed(response.raw().body.toString())
            }

            onLoading(false)
        } catch (t: Throwable) {
            when (t) {
                is CancellationException -> {}
                else -> {
                    onLoading(false)
                    onError(t)
                }
            }
        }
    }
}
