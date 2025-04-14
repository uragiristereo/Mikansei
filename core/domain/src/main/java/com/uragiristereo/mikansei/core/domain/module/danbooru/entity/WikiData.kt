package com.uragiristereo.mikansei.core.domain.module.danbooru.entity

import com.uragiristereo.mikansei.core.model.danbooru.Post

data class WikiData(
    val tag: String,
    val wiki: Wiki?,
    val tagCategory: Tag.Category,
    val postCount: Long,
    val posts: List<Post>?,
)
