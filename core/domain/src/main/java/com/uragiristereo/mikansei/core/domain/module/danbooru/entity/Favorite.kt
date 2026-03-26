package com.uragiristereo.mikansei.core.domain.module.danbooru.entity

import com.uragiristereo.mikansei.core.model.danbooru.Post
import kotlinx.serialization.Serializable

sealed class Favorite {
    abstract val id: Int
    abstract val name: String

    data class Regular(
        override val id: Int,
        override val name: String,
        val posts: List<Post>,
        val preferredThumbnailPostId: Int?,
    ) : Favorite()

    @Serializable
    data class Group(
        override val id: Int,
        override val name: String,
        val postIds: List<Int>,
        val thumbnailPost: Post? = null,
        val thumbnailUrl: String? = null,
        val isPostAlreadyExits: Boolean = false,
    ) : Favorite()
}
