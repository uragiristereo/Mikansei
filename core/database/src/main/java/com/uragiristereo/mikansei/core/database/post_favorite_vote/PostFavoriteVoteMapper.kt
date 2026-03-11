package com.uragiristereo.mikansei.core.database.post_favorite_vote

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostVote
import com.uragiristereo.mikansei.core.domain.module.database.entity.PostFavoriteVote

fun PostFavoriteVoteRow.toPostFavoriteVote(): PostFavoriteVote {
    return PostFavoriteVote(
        isInFavorites = isInFavorites,
        voteStatus = when (voteStatus) {
            1 -> PostVote.Status.UPVOTED
            -1 -> PostVote.Status.DOWNVOTED
            else -> PostVote.Status.NONE
        },
    )
}

fun PostFavoriteVote.toPostFavoriteVoteRow(
    postId: Int,
    userId: Int,
): PostFavoriteVoteRow {
    return PostFavoriteVoteRow(
        postId = postId,
        userId = userId,
        isInFavorites = isInFavorites,
        voteStatus = when (voteStatus) {
            PostVote.Status.UPVOTED -> 1
            PostVote.Status.DOWNVOTED -> -1
            PostVote.Status.NONE -> 0
        },
    )
}
