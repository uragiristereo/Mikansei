package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostsResult2
import com.uragiristereo.mikansei.core.domain.module.database.PostRepository
import com.uragiristereo.mikansei.core.domain.module.database.SessionRepository
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import kotlinx.coroutines.flow.first

class GetPostsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val postRepository: PostRepository,
    private val sessionRepository: SessionRepository,
    private val filterPostsUseCase: FilterPostsUseCase,
) {
    suspend operator fun invoke(
        sessionId: String,
        tags: String,
        page: Int,
    ): Result<PostsResult2> {
        return danbooruRepository.getPosts(tags, page)
            .mapSuccess { postsResult ->
                // if page == 1, it means refreshing and existing posts should be cleared
                val existingPosts = when {
                    page > 1 -> {
                        filterPostsUseCase(
                            posts = sessionRepository.getPosts(sessionId).first(),
                            tags = tags,
                        )
                    }

                    else -> emptyList()
                }
                val newPosts = filterPostsUseCase(postsResult.posts, tags)
                var canLoadMore = postsResult.canLoadMore

                if (canLoadMore && newPosts.isEmpty()) {
                    canLoadMore = false
                }

                postRepository.update(postsResult.posts)
                sessionRepository.updatePosts(
                    sessionId = sessionId,
                    posts = existingPosts + newPosts,
                )

                PostsResult2(
                    isEmpty = postsResult.posts.isEmpty(),
                    canLoadMore = canLoadMore,
                )
            }
    }
}
