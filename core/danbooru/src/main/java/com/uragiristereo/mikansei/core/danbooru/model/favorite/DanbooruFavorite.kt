package com.uragiristereo.mikansei.core.danbooru.model.favorite


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DanbooruFavorite(
    @SerialName("id")
    val id: Int,

    @SerialName("user_id")
    val userId: Int,

    @SerialName("post_id")
    val postId: Int,
)
