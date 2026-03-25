package com.uragiristereo.mikansei.core.domain.module.danbooru.entity

data class FavoriteGroupResult(
    val isFromCache: Boolean,
    val items: List<Favorite.Group>,
)
