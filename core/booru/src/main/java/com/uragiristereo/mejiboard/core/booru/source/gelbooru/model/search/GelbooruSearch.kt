package com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GelbooruSearch(
    val category: String,
    val label: String,

    @SerialName(value = "post_count")
    val postCount: Int,

    val type: String,
    val value: String,
)
