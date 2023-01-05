package com.uragiristereo.mejiboard.domain.entity.booru.post

data class PostsResult(
    val data: List<Post> = emptyList(),
    val canLoadMore: Boolean = true,
    val errorMessage: String? = null,
)
