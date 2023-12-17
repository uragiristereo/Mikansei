package com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.core

sealed class NewFavGroupState {
    object Success : NewFavGroupState()

    data class Failed(val message: String) : NewFavGroupState()
}
