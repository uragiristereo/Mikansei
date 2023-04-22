package com.uragiristereo.mikansei.feature.image

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.uragiristereo.safer.compose.navigation.core.getData
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute

class ViewerViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val post = checkNotNull(savedStateHandle.getData<MainRoute.Image>()).post

    var areAppBarsVisible by mutableStateOf(true)
        private set

    fun setAppBarsVisible(value: Boolean) {
        areAppBarsVisible = value
    }
}
