package com.uragiristereo.mejiboard.domain.entity.post

import java.util.Date

data class Post(
    val id: Int,
    val createdAt: Date,
    val uploaderId: Int,
    val source: String?,

    val score: Int,
    val upScore: Int,
    val downScore: Int,
    val favoriteCount: Int,

    val hasScaled: Boolean,
    val imageWidth: Int,
    val imageHeight: Int,
    val aspectRatio: Float,
    val originalFileUrl: String,
    val scaledFileUrl: String,
    val previewFileUrl: String,
    val fileType: String,

    val tags: List<String>,
)
