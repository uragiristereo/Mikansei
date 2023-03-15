package com.uragiristereo.mikansei.core.danbooru.model.tag


import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class DanbooruTagAutoComplete(
    @SerialName("type")
    val type: String,

    @SerialName("label")
    val label: String,

    @SerialName("value")
    val value: String,

    @SerialName("category")
    val category: Int,

    @SerialName("post_count")
    val postCount: Long,

    @SerialName("antecedent")
    val antecedent: String? = null,
)
