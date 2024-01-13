package com.uragiristereo.mikansei.feature.image

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.serializednavigationextension.runtime.navArgsOf

class ViewerViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val post = checkNotNull(savedStateHandle.navArgsOf<MainRoute.Image>()).post

    var areAppBarsVisible by mutableStateOf(true)
        private set

    fun setAppBarsVisible(value: Boolean) {
        areAppBarsVisible = value
    }

    fun toggleAppBarsVisible() {
        areAppBarsVisible = !areAppBarsVisible
    }
}
