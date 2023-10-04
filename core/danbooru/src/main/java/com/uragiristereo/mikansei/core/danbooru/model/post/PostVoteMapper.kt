package com.uragiristereo.mikansei.core.danbooru.model.post

import com.uragiristereo.mikansei.core.danbooru.model.post.vote.DanbooruPostVote
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostVote

fun List<DanbooruPostVote>.toPostVote(
    postId: Int,
    userId: Int,
): PostVote {
    val postVote = firstOrNull { it.postId == postId && it.userId == userId }

    return PostVote(
        postId = postId,
        userId = userId,
        status = when {
            postVote == null -> PostVote.Status.NONE
            postVote.score == 1 -> PostVote.Status.UPVOTED
            else -> PostVote.Status.DOWNVOTED
        },
    )
}
