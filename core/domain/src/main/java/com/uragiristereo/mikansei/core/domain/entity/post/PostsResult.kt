package com.uragiristereo.mikansei.core.domain.entity.post

import com.uragiristereo.mikansei.core.model.danbooru.post.Post

data class PostsResult(
    val posts: List<Post>,
    val canLoadMore: Boolean,
)
