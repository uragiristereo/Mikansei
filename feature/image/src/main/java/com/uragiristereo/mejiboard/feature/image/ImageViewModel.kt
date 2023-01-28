package com.uragiristereo.mejiboard.feature.image

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.uragiristereo.safer.compose.navigation.core.getData
import com.uragiristereo.mejiboard.core.model.booru.post.PostImage
import com.uragiristereo.mejiboard.core.network.PreferencesRepository
import com.uragiristereo.mejiboard.core.network.model.DetailSizePreference
import com.uragiristereo.mejiboard.core.ui.navigation.MainRoute
import com.uragiristereo.mejiboard.feature.image.core.ImageLoadingState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ImageViewModel(
    savedStateHandle: SavedStateHandle,
    preferencesRepository: PreferencesRepository,
) : ViewModel() {
    val post = savedStateHandle.getData<MainRoute.Image>()!!.post

    var appBarsVisible by mutableStateOf(true)
    val offsetY = Animatable(initialValue = 0f)

    var detailSize by mutableStateOf(preferencesRepository.data.detailSize)

    var showOriginalImage by mutableStateOf(detailSize == DetailSizePreference.KEY_ORIGINAL)
    var originalImageShown by mutableStateOf(false)

    // image post
    var loading by mutableStateOf(ImageLoadingState.DISABLED)
    var isPressed by mutableStateOf(false)
    var fingerCount by mutableStateOf(1)
    var currentZoom by mutableStateOf(1f)

    init {
        preferencesRepository.flowData
            .onEach { preferences ->
                detailSize = preferences.detailSize
                showOriginalImage = detailSize == DetailSizePreference.KEY_ORIGINAL
            }
            .launchIn(viewModelScope)
    }

    fun resizeImage(postImage: PostImage): Pair<Int, Int> {
        val maxSize = 4096f

        return when {
            postImage.width > maxSize || postImage.height > maxSize -> {
                val scale = when {
                    postImage.width > postImage.height -> maxSize.div(postImage.width)
                    else -> maxSize.div(postImage.height)
                }

                val scaledWidth = postImage.width * scale
                val scaledHeight = postImage.height * scale

                Pair(scaledWidth.toInt(), scaledHeight.toInt())
            }

            else -> Pair(postImage.width, postImage.height)
        }
    }
}
