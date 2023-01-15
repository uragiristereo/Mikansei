package com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.tag

import kotlinx.serialization.Serializable

@Serializable
data class GelbooruTag(
    val ambiguous: Int,
    val count: Int,
    val id: Int,
    val name: String,
    val type: Int,
)
