package com.uragiristereo.mikansei.core.model.danbooru.post

import com.uragiristereo.mikansei.core.model.Rating
import com.uragiristereo.mikansei.core.model.danbooru.DanbooruDateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Post(
    val id: Int,

    @Serializable(DanbooruDateSerializer::class)
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
    val hasScaled: Boolean,

    val image: PostImage,
    val scaledImage: PostImage,
    val previewImage: PostImage,

    val aspectRatio: Float,

    val tags: List<String>,
)
