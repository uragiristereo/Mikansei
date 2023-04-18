package com.uragiristereo.mikansei.core.domain.entity.post.vote

import com.uragiristereo.mikansei.core.danbooru.model.post.vote.DanbooruPostVote

fun DanbooruPostVote.toPostVote(): PostVote {
    return PostVote(
        id = id,
        postId = postId,
        state = when (score) {
            1 -> PostVoteState.UPVOTED
            else -> PostVoteState.DOWNVOTED
        },
    )
}

fun List<DanbooruPostVote>.toPostVoteList(): List<PostVote> {
    return map { it.toPostVote() }
}
