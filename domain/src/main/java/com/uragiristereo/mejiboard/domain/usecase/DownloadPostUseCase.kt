package com.uragiristereo.mejiboard.domain.usecase

import com.uragiristereo.mejiboard.core.download.DownloadRepository
import com.uragiristereo.mejiboard.core.download.model.DownloadResource
import com.uragiristereo.mejiboard.core.model.ShareOption
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample

class DownloadPostUseCase(
    private val downloadRepository: DownloadRepository,
) {
    @OptIn(FlowPreview::class)
    operator fun invoke(
        post: Post,
        shareOption: ShareOption,
    ): Flow<DownloadResource> {
        return flow {
            val url = when {
                shareOption == ShareOption.COMPRESSED && post.scaled -> post.scaledImage.url
                else -> post.originalImage.url
            }

            downloadRepository.download(url)
                .onEach { resource ->
                    when (resource) {
                        is DownloadResource.Downloading -> {}
                        else -> emit(resource)
                    }
                }
                .sample(periodMillis = 1000L)
                .onEach { resource ->
                    if (resource is DownloadResource.Downloading) {
                        emit(resource)
                    }
                }
        }
    }
}
