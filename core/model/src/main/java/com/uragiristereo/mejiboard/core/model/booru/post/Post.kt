package com.uragiristereo.mejiboard.core.model.booru.post

import androidx.compose.runtime.Stable
import com.uragiristereo.mejiboard.core.model.DateSerializer
import com.uragiristereo.mejiboard.core.model.booru.BooruSource
import kotlinx.serialization.Serializable
import java.util.Date

@Stable
data class Post(
    val source: BooruSource,
    val id: Int,
    val scaled: Boolean,
    val rating: Rating,
    val tags: String,

    @Serializable(with = DateSerializer::class)
    val uploadedAt: Date,

    val uploader: String,
    val imageSource: String,
    val originalImage: PostImage,
    val scaledImage: PostImage,
    val previewImage: PostImage,
    val aspectRatio: Float,
)
