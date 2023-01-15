package com.uragiristereo.mejiboard.core.booru.source.danbooru.model.tag

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DanbooruTag(
    val id: Int,
    val name: String,

    @SerialName(value = "post_count")
    val postCount: Int,

    val category: Int,
)
