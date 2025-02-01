package com.uragiristereo.mikansei.feature.image

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.PostNavType

class ViewerViewModel(
    savedStateHandle: SavedStateHandle,
    userRepository: UserRepository,
) : ViewModel() {
    val post = savedStateHandle.toRoute<MainRoute.Image>(PostNavType).post

    val activeUser = userRepository.active

    var areAppBarsVisible by mutableStateOf(true)
        private set

    fun setAppBarsVisible(value: Boolean) {
        areAppBarsVisible = value
    }

    fun toggleAppBarsVisible() {
        areAppBarsVisible = !areAppBarsVisible
    }
}
