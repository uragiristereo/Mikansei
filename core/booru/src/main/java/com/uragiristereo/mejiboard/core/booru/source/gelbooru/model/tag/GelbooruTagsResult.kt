package com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.tag

import kotlinx.serialization.Serializable

@Serializable
data class GelbooruTagsResult(
    val tag: List<GelbooruTag>?,
)
