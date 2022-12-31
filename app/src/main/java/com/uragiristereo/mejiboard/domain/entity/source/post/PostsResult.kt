package com.uragiristereo.mejiboard.domain.entity.source.post

data class PostsResult(
    val data: List<Post> = emptyList(),
    val canLoadMore: Boolean = true,
    val errorMessage: String? = null,
)
