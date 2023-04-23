package com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.core

sealed class FavoriteGroupSubmitState {
    data class Success(val favoriteGroupName: String) : FavoriteGroupSubmitState()

    data class Failed(val message: String) : FavoriteGroupSubmitState()
}
