package com.uragiristereo.mikansei.core.domain.module.database

import com.uragiristereo.mikansei.core.domain.module.database.entity.PostFavoriteVote
import kotlinx.coroutines.flow.Flow

interface PostFavoriteVoteRepository {
    fun get(postId: Int, userId: Int): Flow<PostFavoriteVote>

    suspend fun update(postId: Int, userId: Int, item: PostFavoriteVote)
}
