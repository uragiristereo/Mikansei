package com.uragiristereo.mikansei.core.danbooru.model.autocomplete


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DanbooruAutoComplete(
    // enum = [tag_query, wiki_page]
    @SerialName("type")
    val type: String,

    @SerialName("label")
    val label: String,

    @SerialName("value")
    val value: String,

    // type = [tag_query, wiki_page]
    @SerialName("category")
    val category: Int? = null,

    // type = [tag_query]
    @SerialName("post_count")
    val postCount: Long? = null,

    // type = [tag_query]
    @SerialName("antecedent")
    val antecedent: String? = null,
)
