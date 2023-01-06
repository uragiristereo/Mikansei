package com.uragiristereo.mejiboard.core.model.booru.post

data class PostsResult(
    val data: List<Post> = emptyList(),
    val canLoadMore: Boolean = true,
    val errorMessage: String? = null,
)
