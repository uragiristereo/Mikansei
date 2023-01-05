package com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.post

import com.google.gson.annotations.SerializedName
import com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.common.GelbooruAttributes

data class GelbooruPostsResult(
    @SerializedName(value = "@attributes")
    val attributes: GelbooruAttributes,

    val post: List<GelbooruPost>?,
)
