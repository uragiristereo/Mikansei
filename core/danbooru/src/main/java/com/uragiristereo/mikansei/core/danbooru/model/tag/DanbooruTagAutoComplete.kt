package com.uragiristereo.mikansei.core.danbooru.model.tag


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DanbooruTagAutoComplete(
    @SerialName("type")
    val type: String,

    @SerialName("label")
    val label: String,

    @SerialName("value")
    val value: String,

    @SerialName("category")
    val category: Int? = null,

    @SerialName("post_count")
    val postCount: Long? = null,

    @SerialName("antecedent")
    val antecedent: String? = null,
)
