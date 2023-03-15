package com.uragiristereo.mejiboard.domain.usecase_new

import com.uragiristereo.mejiboard.core.model.Constants
import com.uragiristereo.mejiboard.core.preferences.PreferencesRepository
import com.uragiristereo.mejiboard.domain.entity.post.Post
import com.uragiristereo.mejiboard.domain.entity.post.PostsResult
import com.uragiristereo.mejiboard.domain.entity.post.toPostList
import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import com.uragiristereo.mikansei.core.danbooru.result.Result
import com.uragiristereo.mikansei.core.danbooru.result.mapSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GetPostsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val preferencesRepository: PreferencesRepository,
) {
    suspend operator fun invoke(
        tags: String,
        pageId: Int,
        currentPosts: List<Post>,
    ): Flow<Result<PostsResult>> {
        return danbooruRepository.getPosts(tags, pageId)
            .mapSuccess { danbooruPosts ->
                // if page == 0, it means refreshing and previous posts should be cleared
                val previousPosts = when (pageId) {
                    0 -> listOf()
                    else -> currentPosts
                }

                val filters = preferencesRepository.flowData
                    .first()
                    .blacklistedTags

                val posts = danbooruPosts.toPostList()
                var filtered = posts

                if (filters.isNotEmpty()) {
                    filtered = posts.filter { post ->
                        !filters.any { tag ->
                            post.tags.contains(tag.lowercase())
                        }
                    }
                }

                var canLoadMore = (posts.size == Constants.POSTS_PER_PAGE)

                if (canLoadMore && filtered.isEmpty()) {
                    canLoadMore = false
                }

                // remove duplicate posts
                val combined = previousPosts.plus(filtered).distinctBy { it.id }

                PostsResult(
                    data = combined,
                    canLoadMore = canLoadMore,
                )
            }
    }
}
