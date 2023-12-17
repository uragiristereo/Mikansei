package com.uragiristereo.mikansei.feature.home.favorites.favgroup.addto.core

data class FavoriteGroup(
    val id: Int,
    val name: String,
    val thumbnailUrl: String?,
    val isPostAlreadyExits: Boolean,
)
