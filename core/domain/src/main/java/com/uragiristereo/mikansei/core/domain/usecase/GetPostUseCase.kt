package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.Environment
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.danbooru.Rating
import com.uragiristereo.mikansei.core.model.result.Result

class GetPostUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userRepository: UserRepository,
    private val environment: Environment,
) {
    suspend operator fun invoke(postId: Int): Result<Post> {
        return when (val postResult = danbooruRepository.getPost(postId)) {
            is Result.Success -> {
                val post = postResult.data
                val activeUser = userRepository.active.value

                return when {
                    post.rating != Rating.GENERAL && environment.safeMode -> {
                        Result.Failed("This post can't be viewed on this environment")
                    }

                    post.rating in activeUser.mikansei.postsRatingFilter.getFilteredRatings() -> {
                        Result.Failed("This post can't be viewed on the current rating filter")
                    }

                    else -> postResult
                }
            }

            is Result.Failed -> Result.Failed(postResult.message)
            is Result.Error -> Result.Error(postResult.t)
        }
    }
}
