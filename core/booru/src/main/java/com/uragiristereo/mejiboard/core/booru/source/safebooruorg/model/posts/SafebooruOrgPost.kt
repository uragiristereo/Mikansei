package com.uragiristereo.mejiboard.core.booru.source.safebooruorg.model.posts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SafebooruOrgPost(
    val directory: String,
    val height: Int,
    val id: Int,
    val image: String,
    val change: Int,
    val owner: String,

    val rating: String,
    val sample: Boolean,

    @SerialName(value = "sample_height")
    val sampleHeight: Int,

    @SerialName(value = "sample_width")
    val sampleWidth: Int,

    val tags: String,
    val width: Int,
)
