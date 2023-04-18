package com.uragiristereo.mikansei.core.domain.entity.post.vote

data class PostVote(
    val id: Int,
    val postId: Int,
    val state: PostVoteState,
)
