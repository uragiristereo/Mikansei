package com.uragiristereo.mikansei.core.database.post

import com.uragiristereo.mikansei.core.domain.module.database.PostRepository
import com.uragiristereo.mikansei.core.model.danbooru.Post
import kotlinx.coroutines.flow.Flow

class PostRepositoryImpl(private val postDao: PostDao) : PostRepository {
    override fun getUploaderName(postId: Int): Flow<String?> {
        return postDao.getUploaderName(postId)
    }

    override suspend fun update(post: Post) {
        postDao.update(postId = post.id, post = post)
    }

    override suspend fun update(posts: List<Post>) {
        postDao.update(posts)
    }

    override suspend fun updateUploaderName(postId: Int, uploaderName: String) {
        postDao.updateUploaderName(postId = postId, uploaderName = uploaderName)
    }

    override suspend fun reset() {
        postDao.reset()
    }
}
