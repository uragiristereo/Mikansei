package com.uragiristereo.mikansei.core.model.danbooru.post

import androidx.compose.runtime.Stable
import com.uragiristereo.mikansei.core.model.danbooru.DanbooruDateSerializer
import com.uragiristereo.mikansei.core.model.danbooru.Rating
import kotlinx.serialization.Serializable
import java.util.*

@Stable
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
    val isBanned: Boolean,
    val hasScaled: Boolean,

    val image: PostImage,
    val scaledImage: PostImage,
    val previewImage: PostImage,

    val aspectRatio: Float,

    val tags: List<String>,
)
