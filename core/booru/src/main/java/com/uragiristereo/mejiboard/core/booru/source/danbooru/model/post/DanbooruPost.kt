package com.uragiristereo.mejiboard.core.booru.source.danbooru.model.post

import com.uragiristereo.mejiboard.core.booru.source.danbooru.model.DanbooruDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class DanbooruPost(
    val id: Int?,

    @SerialName(value = "created_at")
    @Serializable(with = DanbooruDateSerializer::class)
    val createdAt: Date,

    @SerialName(value = "uploader_id")
    val uploaderId: Int,
    val source: String,
    val md5: String? = null,
    val rating: String,

    @SerialName(value = "image_width")
    val imageWidth: Int,

    @SerialName(value = "image_height")
    val imageHeight: Int,

    @SerialName(value = "tag_string")
    val tagString: String,

    @SerialName(value = "file_ext")
    val fileExt: String,

    @SerialName(value = "has_large")
    val hasLarge: Boolean? = null,
)
