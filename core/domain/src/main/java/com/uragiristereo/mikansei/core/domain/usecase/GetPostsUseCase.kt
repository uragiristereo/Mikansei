package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostsResult
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.danbooru.Rating
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess

class GetPostsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        tags: String,
        page: Int,
        currentPosts: List<Post>,
    ): Result<PostsResult> {
        return danbooruRepository.getPosts(tags, page)
            .mapSuccess { postsResult ->
                // if page == 1, it means refreshing and previous posts should be cleared
                val previousPosts = when (page) {
                    1 -> listOf()
                    else -> currentPosts
                }

                val profile = userRepository.active.value
                val ratingFilters = profile.mikansei.postsRatingFilter.getFilteredRatings()
                var posts = postsResult.posts
                var canLoadMore = postsResult.canLoadMore

                val showDeletedPosts = when {
                    profile.danbooru.showDeletedPosts || tags.contains("status:any") || tags.contains("status:deleted") -> listOf(false, true)
                    else -> listOf(false)
                }

                posts = posts.filter { post ->
                    !profile.danbooru.blacklistedTags.any { tags ->
                        tags.split(' ')
                            .map { tag ->
                                val isTagWhitelist = tag.take(1) == "-"

                                val tagWithoutHyphen = when {
                                    isTagWhitelist -> tag.substring(startIndex = 1)
                                    else -> tag
                                }

                                val result = when (tagWithoutHyphen) {
                                    "rating:g", "rating:general" -> post.rating == Rating.GENERAL
                                    "rating:s", "rating:sensitive" -> post.rating == Rating.SENSITIVE
                                    "rating:q", "rating:questionable" -> post.rating == Rating.QUESTIONABLE
                                    "rating:e", "rating:explicit" -> post.rating == Rating.EXPLICIT
                                    "status:pending" -> post.status == Post.Status.PENDING

                                    else -> post.tags.contains(tagWithoutHyphen.lowercase())
                                }

                                when {
                                    isTagWhitelist -> !result
                                    else -> result
                                }
                            }.none { !it }
                    }
                            && post.rating !in ratingFilters
                            && post.status != Post.Status.BANNED
                            && (post.status == Post.Status.DELETED) in showDeletedPosts
                }

                if (canLoadMore && posts.isEmpty()) {
                    canLoadMore = false
                }

                // remove duplicate posts
                val combined = previousPosts.plus(posts).distinctBy { it.id }

                PostsResult(
                    posts = combined,
                    canLoadMore = canLoadMore,
                )
            }
    }
}
