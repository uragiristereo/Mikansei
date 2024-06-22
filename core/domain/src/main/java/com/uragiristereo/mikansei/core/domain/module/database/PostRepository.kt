package com.uragiristereo.mikansei.core.domain.module.database

import com.uragiristereo.mikansei.core.model.danbooru.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getUploaderName(postId: Int): Flow<String?>

    suspend fun update(post: Post)

    suspend fun update(posts: List<Post>)

    suspend fun updateUploaderName(postId: Int, uploaderName: String)

    suspend fun reset()
}
