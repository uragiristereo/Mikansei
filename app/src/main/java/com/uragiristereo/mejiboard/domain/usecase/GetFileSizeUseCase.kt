package com.uragiristereo.mejiboard.domain.usecase

import com.uragiristereo.mejiboard.domain.repository.NetworkRepository
import kotlinx.coroutines.CancellationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class GetFileSizeUseCase : KoinComponent {
    private val networkRepository: NetworkRepository = get()

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
