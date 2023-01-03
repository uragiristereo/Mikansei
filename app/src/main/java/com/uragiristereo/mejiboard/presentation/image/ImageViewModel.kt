package com.uragiristereo.mejiboard.presentation.image

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mejiboard.data.preferences.PreferencesRepository
import com.uragiristereo.mejiboard.data.preferences.entity.DetailSizePreference
import com.uragiristereo.mejiboard.domain.entity.source.post.Post
import com.uragiristereo.mejiboard.presentation.common.navigation.getData
import com.uragiristereo.mejiboard.presentation.image.core.ImageLoadingState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ImageViewModel(
    savedStateHandle: SavedStateHandle,
    preferencesRepository: PreferencesRepository,
) : ViewModel() {
    val post = savedStateHandle.getData<Post>(key = "post")!!

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
