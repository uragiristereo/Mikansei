package com.uragiristereo.mikansei.core.model.booru.post

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class PostImage(
    val url: String,
    val fileType: String,
    val height: Int,
    val width: Int,
)
