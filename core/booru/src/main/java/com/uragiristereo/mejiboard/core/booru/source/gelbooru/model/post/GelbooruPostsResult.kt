package com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.post

import com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.common.GelbooruAttributes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GelbooruPostsResult(
    @SerialName(value = "@attributes")
    val attributes: GelbooruAttributes,

    val post: List<GelbooruPost>?,
)
