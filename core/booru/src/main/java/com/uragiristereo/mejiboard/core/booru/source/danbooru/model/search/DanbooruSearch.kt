package com.uragiristereo.mejiboard.core.booru.source.danbooru.model.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DanbooruSearch(
    val value: String,
    val category: Int,

    @SerialName(value = "post_count")
    val postCount: Int,

    val antecedent: String? = null,
)
