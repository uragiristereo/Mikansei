package com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.post

import com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.serializer.GelbooruAnySerializer
import com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.serializer.GelbooruDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class GelbooruPost(
    @SerialName(value = "created_at")
    @Serializable(with = GelbooruDateSerializer::class)
    val createdAt: Date,

    val directory: String,

    @SerialName(value = "file_url")
    val fileUrl: String,

    val height: Int,
    val id: Int,
    val image: String,
    val md5: String,
    val owner: String,

    @SerialName(value = "preview_height")
    val previewHeight: Int,

    @SerialName(value = "preview_width")
    val previewWidth: Int,

    val rating: String,
    val sample: Int,

    @SerialName(value = "sample_height")
    val sampleHeight: Int,

    @SerialName(value = "sample_width")
    val sampleWidth: Int,

    @Serializable(with = GelbooruAnySerializer::class)
    val source: Any,

    val tags: String,

    val width: Int,
)
