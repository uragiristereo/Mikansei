package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostVote
import com.uragiristereo.mikansei.core.domain.module.database.PostRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.domain.module.database.entity.PostFavoriteVote
import com.uragiristereo.mikansei.core.model.Environment
import com.uragiristereo.mikansei.core.model.danbooru.Rating
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.combineSuccess

class GetPostWithFavoriteVoteUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val environment: Environment,
) {
    suspend operator fun invoke(postId: Int): Result<PostFavoriteVote> {
        val activeUser = userRepository.active.value

        return when (val postResult = danbooruRepository.getPost(postId)) {
            is Result.Success -> {
                val post = postResult.data

                return when {
                    post.rating != Rating.GENERAL && environment.safeMode -> {
                        Result.Failed("This post can't be viewed on this environment")
                    }

                    post.rating in activeUser.mikansei.postsRatingFilter.getFilteredRatings() -> {
                        Result.Failed("This post can't be viewed on the current rating filter")
                    }

                    else -> {
                        val favoriteVoteResult = when {
                            activeUser.isNotAnonymous() -> combineSuccess(
                                first = {
                                    danbooruRepository.isPostInFavorites(postId, activeUser.id)
                                },
                                other = {
                                    danbooruRepository.getPostVote(postId, activeUser.id)
                                },
                            ) { favorite, vote ->
                                PostFavoriteVote(
                                    isInFavorites = favorite,
                                    voteStatus = vote.status,
                                )
                            }

                            else -> Result.Success(
                                PostFavoriteVote(
                                    isInFavorites = false,
                                    voteStatus = PostVote.Status.NONE,
                                )
                            )
                        }

                        postRepository.update(listOf(post))

                        favoriteVoteResult
                    }
                }
            }

            is Result.Failed -> Result.Failed(postResult.message)
            is Result.Error -> Result.Error(postResult.t)
        }
    }
}
