package com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.search

import com.google.gson.annotations.SerializedName

data class GelbooruSearch(
    val category: String,
    val label: String,

    @SerializedName(value = "post_count")
    val postCount: Int,

    val type: String,
    val value: String,
)
