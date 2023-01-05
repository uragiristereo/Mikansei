package com.uragiristereo.mejiboard.core.booru.source.safebooruorg.model.posts

import com.google.gson.annotations.SerializedName

data class SafebooruOrgPost(
    val directory: String,
    val hash: String,
    val height: Int,
    val id: Int,
    val image: String,
    val change: Int,
    val owner: String,

    @SerializedName(value = "parent_id")
    val parentId: Int,

    val rating: String,
    val sample: Boolean,

    @SerializedName(value = "sample_height")
    val sampleHeight: Int,

    @SerializedName(value = "sample_width")
    val sampleWidth: Int,

    val score: Int?,
    val tags: String,
    val width: Int,
)
