package com.uragiristereo.mikansei.core.domain.module.danbooru.entity

data class FavoriteResult(
    val isFromCache: Boolean,
    val items: List<Favorite>,
)
