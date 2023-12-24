package com.uragiristereo.mikansei.core.domain.module.danbooru.entity

import kotlinx.serialization.Serializable

@Serializable
data class Favorite(
    val id: Int,
    val name: String,
    val thumbnailUrl: String?,
    val postIds: List<Int>,
)
