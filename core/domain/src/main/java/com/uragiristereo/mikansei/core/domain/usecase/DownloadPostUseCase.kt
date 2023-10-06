package com.uragiristereo.mikansei.core.domain.usecase

import android.net.Uri
import com.uragiristereo.mikansei.core.domain.module.network.DownloadRepository
import com.uragiristereo.mikansei.core.domain.module.network.entity.DownloadResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DownloadPostUseCase(
    private val downloadRepository: DownloadRepository,
) {
    operator fun invoke(
        postId: Int,
        url: String,
        uri: Uri,
    ): Flow<DownloadResource> {
        return channelFlow {
            val job = downloadRepository.download(
                postId = postId,
                url = url,
                uri = uri,
                sample = 1000L,
            )
                .onEach { resource ->
                    send(resource)
                }
                .launchIn(CoroutineScope(Dispatchers.Default))

            job.join()
        }
    }
}
