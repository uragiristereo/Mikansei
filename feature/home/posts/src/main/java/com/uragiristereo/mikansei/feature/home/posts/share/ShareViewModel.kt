package com.uragiristereo.mikansei.feature.home.posts.share

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.network.NetworkRepository
import com.uragiristereo.mikansei.core.domain.usecase.ConvertFileSizeUseCase
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.PostNavType
import kotlinx.coroutines.launch

class ShareViewModel(
    savedStateHandle: SavedStateHandle,
    private val networkRepository: NetworkRepository,
    private val convertFileSizeUseCase: ConvertFileSizeUseCase,
    danbooruRepository: DanbooruRepository,
) : ViewModel() {
    val navArgs = savedStateHandle.toRoute<HomeRoute.Share>(PostNavType)

    val postLink = danbooruRepository.host.parsePostLink(navArgs.post.id)

    var scaledImageFileSizeStr by mutableStateOf("")
        private set

    var originalImageFileSizeStr by mutableStateOf("")
        private set

    init {
        getImagesFileSize()
    }

    private fun getImagesFileSize() {
        viewModelScope.launch {
            launch {
                navArgs.post.medias.scaled?.let { media ->
                    scaledImageFileSizeStr = "Loading..."

                    networkRepository.getFileSize(media.url).collect { result ->
                        scaledImageFileSizeStr = when (result) {
                            is Result.Success -> convertFileSizeUseCase(result.data)
                            else -> "Error!"
                        }
                    }
                }
            }

            launch {
                originalImageFileSizeStr = "Loading..."

                networkRepository.getFileSize(navArgs.post.medias.original.url).collect { result ->
                    originalImageFileSizeStr = when (result) {
                        is Result.Success -> convertFileSizeUseCase(result.data)
                        else -> "Error!"
                    }
                }
            }
        }
    }
}
