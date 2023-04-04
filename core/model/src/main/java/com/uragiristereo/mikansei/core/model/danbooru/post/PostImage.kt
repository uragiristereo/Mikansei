package com.uragiristereo.mikansei.core.model.danbooru.post

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class PostImage(
    val url: String,
    val width: Int,
    val height: Int,
    val fileType: String,
)
