package com.uragiristereo.mikansei.feature.home.favorites.new_fav_group.core

sealed class NewFavGroupState {
    object Success : NewFavGroupState()

    data class Failed(val message: String) : NewFavGroupState()
}
