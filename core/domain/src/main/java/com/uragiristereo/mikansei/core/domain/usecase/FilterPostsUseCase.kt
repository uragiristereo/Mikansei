package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.danbooru.Rating

class FilterPostsUseCase(private val userRepository: UserRepository) {
    operator fun invoke(posts: List<Post>, tags: String): List<Post> {
        val profile = userRepository.active.value
        val ratingFilters = profile.mikansei.postsRatingFilter.getFilteredRatings()
        val showDeletedPosts = when {
            profile.danbooru.showDeletedPosts
                .or(tags.contains("status:any"))
                .or(tags.contains("status:deleted")) -> listOf(false, true)
            else -> listOf(false)
        }
        val showPendingPosts = when {
            profile.mikansei.showPendingPosts
                .or(tags.contains("status:any"))
                .or(tags.contains("status:pending")) -> listOf(false, true)
            else -> listOf(false)
        }

        return posts.filter { post ->
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
                    && (post.status == Post.Status.PENDING) in showPendingPosts
        }
    }
}
