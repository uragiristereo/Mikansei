package com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.post

import kotlinx.serialization.Serializable

@Serializable
data class GelbooruPostsResult(
    val post: List<GelbooruPost>?,
)
