package com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.column

data class FavoriteGroup(
    val id: Int,
    val name: String,
    val thumbnailUrl: String?,
    val isPostAlreadyExits: Boolean,
)
