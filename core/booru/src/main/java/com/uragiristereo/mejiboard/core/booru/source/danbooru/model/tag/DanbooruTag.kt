package com.uragiristereo.mejiboard.core.booru.source.danbooru.model.tag

import com.uragiristereo.mejiboard.core.booru.source.danbooru.model.DanbooruDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class DanbooruTag(
    val id: Int,
    val name: String,

    @SerialName(value = "post_count")
    val postCount: Int,

    val category: Int,

    @SerialName(value = "created_at")
    @Serializable(with = DanbooruDateSerializer::class)
    val createdAt: Date,

    @SerialName(value = "updated_at")
    @Serializable(with = DanbooruDateSerializer::class)
    val updatedAt: Date,

    @SerialName(value = "is_deprecated")
    val isDeprecated: Boolean,
)
