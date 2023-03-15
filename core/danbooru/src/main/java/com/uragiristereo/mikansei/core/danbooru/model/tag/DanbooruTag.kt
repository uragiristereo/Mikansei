package com.uragiristereo.mikansei.core.danbooru.model.tag


import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class DanbooruTag(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("post_count")
    val postCount: Long,

    @SerialName("category")
    val category: Int,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("is_deprecated")
    val isDeprecated: Boolean,

    @SerialName("words")
    val words: List<String>,
)
