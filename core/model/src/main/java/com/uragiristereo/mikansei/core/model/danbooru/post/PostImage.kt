package com.uragiristereo.mikansei.core.model.danbooru.post

import kotlinx.serialization.Serializable

@Serializable
data class PostImage(
    val url: String,
    val width: Int,
    val height: Int,
    val fileType: String,
)
