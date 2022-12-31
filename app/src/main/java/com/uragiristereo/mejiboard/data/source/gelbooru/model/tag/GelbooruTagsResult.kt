package com.uragiristereo.mejiboard.data.source.gelbooru.model.tag

import com.google.gson.annotations.SerializedName
import com.uragiristereo.mejiboard.data.source.gelbooru.model.common.GelbooruAttributes

data class GelbooruTagsResult(
    @SerializedName(value = "@attributes")
    val attributes: GelbooruAttributes,

    val tag: List<GelbooruTag>?,
)