package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostsResult
import com.uragiristereo.mikansei.core.domain.module.database.PostRepository
import com.uragiristereo.mikansei.core.domain.module.database.SessionRepository
import com.uragiristereo.mikansei.core.domain.module.database.entity.Session
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import kotlinx.coroutines.flow.first

class GetPostsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val postRepository: PostRepository,
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke(
        sessionId: String,
        tags: String,
        page: Int,
    ): Result<PostsResult> {
        return danbooruRepository.getPosts(tags, page)
            .mapSuccess { postsResult ->
                sessionRepository.addSession(Session(id = sessionId, tags = tags))

                // if page == 1, it means refreshing and existing posts should be cleared
                val existingPosts = when {
                    page > 1 -> sessionRepository.getPosts(sessionId).first()
                    else -> emptyList()
                }
                var canLoadMore = postsResult.canLoadMore

                if (canLoadMore && postsResult.posts.isEmpty()) {
                    canLoadMore = false
                }

                postRepository.update(postsResult.posts)
                sessionRepository.updatePosts(
                    sessionId = sessionId,
                    posts = existingPosts + postsResult.posts,
                )

                PostsResult(
                    posts = existingPosts + postsResult.posts,
                    isEmpty = postsResult.posts.isEmpty(),
                    canLoadMore = canLoadMore,
                )
            }
    }
}
