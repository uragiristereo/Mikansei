package com.uragiristereo.mejiboard.domain.usecase_new

import com.uragiristereo.mejiboard.core.model.Constants
import com.uragiristereo.mejiboard.core.model.result.Result
import com.uragiristereo.mejiboard.core.model.result.mapSuccess
import com.uragiristereo.mejiboard.core.preferences.PreferencesRepository
import com.uragiristereo.mejiboard.domain.entity.post.Post
import com.uragiristereo.mejiboard.domain.entity.post.PostsResult
import com.uragiristereo.mejiboard.domain.entity.post.toPostList
import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GetPostsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val preferencesRepository: PreferencesRepository,
) {
    suspend operator fun invoke(
        tags: String,
        page: Int,
        currentPosts: List<Post>,
    ): Flow<Result<PostsResult>> {
        return danbooruRepository.getPosts(tags, page)
            .mapSuccess { danbooruPosts ->
                // if page == 1, it means refreshing and previous posts should be cleared
                val previousPosts = when (page) {
                    1 -> listOf()
                    else -> currentPosts
                }

                val filters = preferencesRepository.flowData
                    .first()
                    .blacklistedTags

                val posts = danbooruPosts.toPostList()
                var filtered = listOf<Post>()

                if (filters.isNotEmpty()) {
                    filtered = posts.filter { post ->
                        !filters.any { tag ->
                            post.tags.contains(tag.lowercase())
                        }
                    }
                }

                var canLoadMore = posts.size == Constants.POSTS_PER_PAGE

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
