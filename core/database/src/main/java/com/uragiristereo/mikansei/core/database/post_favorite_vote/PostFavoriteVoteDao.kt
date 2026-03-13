package com.uragiristereo.mikansei.core.database.post_favorite_vote

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PostFavoriteVoteDao {
    @Query("select * from post_favorite_votes where post_id = :postId and user_id = :userId limit 1")
    fun get(postId: Int, userId: Int): Flow<PostFavoriteVoteRow?>

    @Upsert
    suspend fun update(item: PostFavoriteVoteRow)
}
