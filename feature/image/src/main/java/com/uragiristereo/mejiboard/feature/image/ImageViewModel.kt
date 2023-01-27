package com.uragiristereo.mejiboard.feature.image

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.uragiristereo.safer.compose.navigation.core.getData
import com.uragiristereo.mejiboard.core.preferences.PreferencesRepository
import com.uragiristereo.mejiboard.core.preferences.model.DetailSizePreference
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
}
