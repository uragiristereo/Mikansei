package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.database.dao.user.toUser
import com.uragiristereo.mikansei.core.domain.entity.post.PostsResult
import com.uragiristereo.mikansei.core.domain.entity.post.toPostList
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GetPostsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userDao: UserDao,
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

                val user = userDao.getActive()
                    .first()
                    .toUser()

                val ratingFilters = user.postsRatingFilter.getFilteredRatings()

                var posts = danbooruPosts.toPostList()
                var canLoadMore = posts.size == Constants.POSTS_PER_PAGE

                val showDeletedPosts = when {
                    user.showDeletedPosts || tags.contains("status:any") || tags.contains("status:deleted") -> listOf(false, true)
                    else -> listOf(false)
                }

                posts = posts.filter { post ->
                    !user.blacklistedTags.any { tag ->
                        post.tags.contains(tag.lowercase())
                    } && post.rating !in ratingFilters && !post.isBanned && post.isDeleted in showDeletedPosts
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
