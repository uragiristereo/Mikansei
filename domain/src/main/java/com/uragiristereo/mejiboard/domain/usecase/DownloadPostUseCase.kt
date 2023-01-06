package com.uragiristereo.mejiboard.domain.usecase

import com.uragiristereo.mejiboard.core.download.DownloadRepository
import com.uragiristereo.mejiboard.core.download.model.DownloadResource
import com.uragiristereo.mejiboard.core.model.ShareOption
import com.uragiristereo.mejiboard.core.model.booru.post.Post
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
        post: Post,
        shareOption: ShareOption,
    ): Flow<DownloadResource> {
        val url = when {
            post.scaled && shareOption == ShareOption.COMPRESSED -> post.scaledImage.url
            else -> post.originalImage.url
        }

        return channelFlow {
            val job = downloadRepository.download(
                postId = post.id,
                url = url,
                sample = 1000L
            )
                .onEach { resource ->
                    send(resource)
                }
                .launchIn(CoroutineScope(Dispatchers.Default))

            job.join()
        }
    }
}
