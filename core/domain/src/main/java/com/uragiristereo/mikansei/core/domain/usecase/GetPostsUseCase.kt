package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostsResult
import com.uragiristereo.mikansei.core.domain.module.database.model.toProfile
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.danbooru.Post
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
            .mapSuccess { postsResult ->
                // if page == 1, it means refreshing and previous posts should be cleared
                val previousPosts = when (page) {
                    1 -> listOf()
                    else -> currentPosts
                }

                val profile = userDao.getActive()
                    .first()
                    .toProfile()

                val ratingFilters = profile.mikansei!!.postsRatingFilter.getFilteredRatings()

                var posts = postsResult
                var canLoadMore = posts.size == Constants.POSTS_PER_PAGE

                val showDeletedPosts = when {
                    profile.danbooru.showDeletedPosts || tags.contains("status:any") || tags.contains("status:deleted") -> listOf(false, true)
                    else -> listOf(false)
                }

                posts = posts.filter { post ->
                    !profile.danbooru.blacklistedTags.any { tag ->
                        post.tags.contains(tag.lowercase())
                    } && post.rating !in ratingFilters && post.status != Post.Status.BANNED && (post.status == Post.Status.DELETED) in showDeletedPosts
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
