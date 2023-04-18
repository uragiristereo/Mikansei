package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.entity.post.vote.PostVote
import com.uragiristereo.mikansei.core.domain.entity.post.vote.toPostVote
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import kotlinx.coroutines.flow.Flow

class GetPostVoteUseCase(
    private val danbooruRepository: DanbooruRepository,
) {
    suspend operator fun invoke(
        postId: Int,
        userId: Int,
    ): Flow<Result<PostVote?>> {
        return danbooruRepository.getPostVote(postId, userId).mapSuccess {
            it?.toPostVote()
        }
    }
}
