package com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.common

import kotlinx.serialization.Serializable

@Serializable
data class GelbooruAttributes(
    val count: Int,
    val limit: Int,
    val offset: Int,
)
