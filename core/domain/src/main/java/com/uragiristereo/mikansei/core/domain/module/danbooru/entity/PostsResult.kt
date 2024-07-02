package com.uragiristereo.mikansei.core.domain.module.danbooru.entity

import com.uragiristereo.mikansei.core.model.danbooru.Post

data class PostsResult(
    val posts: List<Post>,
    val isEmpty: Boolean,
    val canLoadMore: Boolean,
)
