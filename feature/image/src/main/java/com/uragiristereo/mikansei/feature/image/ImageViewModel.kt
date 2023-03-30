package com.uragiristereo.mikansei.feature.image

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.uragiristereo.safer.compose.navigation.core.getData
import com.uragiristereo.mikansei.core.model.user.preference.DetailSizePreference
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.image.core.ImageLoadingState
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

    var showOriginalImage by mutableStateOf(detailSize == DetailSizePreference.ORIGINAL)
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
                showOriginalImage = detailSize == DetailSizePreference.ORIGINAL
            }
            .launchIn(viewModelScope)
    }

    fun resizeImage(width: Int, height: Int): Pair<Int, Int> {
        val maxSize = 4096f

        return when {
            width > maxSize || height > maxSize -> {
                val scale = when {
                    width > height -> maxSize.div(width)
                    else -> maxSize.div(height)
                }

                val scaledWidth = width * scale
                val scaledHeight = height * scale

                Pair(scaledWidth.toInt(), scaledHeight.toInt())
            }

            else -> Pair(width, height)
        }
    }
}
