package com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.tag

import com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.common.GelbooruAttributes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GelbooruTagsResult(
    @SerialName(value = "@attributes")
    val attributes: GelbooruAttributes,

    val tag: List<GelbooruTag>?,
)
