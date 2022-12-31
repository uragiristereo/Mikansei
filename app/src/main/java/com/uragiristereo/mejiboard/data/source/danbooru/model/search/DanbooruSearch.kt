package com.uragiristereo.mejiboard.data.source.danbooru.model.search

import com.google.gson.annotations.SerializedName


data class DanbooruSearch(
    val type: String,
    val label: String,
    val value: String,
    val category: Int,

    @SerializedName(value = "post_count")
    val postCount: Int,

    val antecedent: String?,
)