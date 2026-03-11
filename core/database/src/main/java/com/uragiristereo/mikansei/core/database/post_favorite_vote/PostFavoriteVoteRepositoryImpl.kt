package com.uragiristereo.mikansei.core.database.post_favorite_vote

import com.uragiristereo.mikansei.core.domain.module.database.PostFavoriteVoteRepository
import com.uragiristereo.mikansei.core.domain.module.database.entity.PostFavoriteVote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PostFavoriteVoteRepositoryImpl(
    private val postFavoriteVoteDao: PostFavoriteVoteDao,
) : PostFavoriteVoteRepository {
    override fun get(postId: Int, userId: Int): Flow<PostFavoriteVote> {
        return postFavoriteVoteDao.get(postId, userId).map {
            it?.toPostFavoriteVote() ?: PostFavoriteVote()
        }
    }

    override suspend fun update(postId: Int, userId: Int, item: PostFavoriteVote) {
        postFavoriteVoteDao.update(item.toPostFavoriteVoteRow(postId, userId))
    }
}
