package com.uragiristereo.mejiboard.domain.entity.booru.post

import androidx.compose.runtime.Stable

@Stable
data class PostImage(
    val url: String,
    val fileType: String,
    val height: Int,
    val width: Int,
)
