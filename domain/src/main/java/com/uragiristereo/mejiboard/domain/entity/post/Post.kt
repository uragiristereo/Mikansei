package com.uragiristereo.mejiboard.domain.entity.post

import com.uragiristereo.mejiboard.core.model.Rating
import java.util.Date

data class Post(
    val id: Int,
    val createdAt: Date,
    val uploaderId: Int,
    val source: String?,
    val pixivId: Int?,
    val rating: Rating,

    val score: Int,
    val upScore: Int,
    val downScore: Int,
    val favoriteCount: Int,

    val isPending: Boolean,
    val isDeleted: Boolean,

    val imageUrl: String?,
    val imageWidth: Int,
    val imageHeight: Int,
    val imageFileType: String?,

    val scaledImageUrl: String?,
    val scaledImageWidth: Int,
    val scaledImageHeight: Int,
    val scaledImageFileType: String?,

    val previewImageUrl: String?,
    val previewImageWidth: Int,
    val previewImageHeight: Int,
    val previewImageFileType: String?,

    val aspectRatio: Float,

    val tags: List<String>,
)
