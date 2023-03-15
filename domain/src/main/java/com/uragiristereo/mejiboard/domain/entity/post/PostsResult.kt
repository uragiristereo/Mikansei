package com.uragiristereo.mejiboard.domain.entity.post

data class PostsResult(
    val data: List<Post>,
    val canLoadMore: Boolean,
)
