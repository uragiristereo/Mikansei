package com.uragiristereo.mikansei.core.domain.module.danbooru.entity

data class Favorite(
    val id: Int,
    val name: String,
    val thumbnailUrl: String?,
    val postIds: List<Int>,
)
