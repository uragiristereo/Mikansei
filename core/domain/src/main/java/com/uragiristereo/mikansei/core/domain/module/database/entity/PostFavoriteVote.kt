package com.uragiristereo.mikansei.core.domain.module.database.entity

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostVote

data class PostFavoriteVote(
    val isInFavorites: Boolean = false,
    val voteStatus: PostVote.Status = PostVote.Status.NONE,
)
