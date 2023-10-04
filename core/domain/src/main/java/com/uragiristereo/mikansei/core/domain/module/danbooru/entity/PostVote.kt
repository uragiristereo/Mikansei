package com.uragiristereo.mikansei.core.domain.module.danbooru.entity

data class PostVote(
    val postId: Int,
    val userId: Int,
    val status: Status,
) {
    enum class Status {
        NONE,
        UPVOTED,
        DOWNVOTED,
    }
}
