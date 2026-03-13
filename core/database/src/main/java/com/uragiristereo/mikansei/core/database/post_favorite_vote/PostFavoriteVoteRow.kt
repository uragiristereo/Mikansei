package com.uragiristereo.mikansei.core.database.post_favorite_vote

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.time.OffsetDateTime

@Entity(
    tableName = "post_favorite_votes",
    primaryKeys = ["post_id", "user_id"],
)
data class PostFavoriteVoteRow(
    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "post_id")
    val postId: Int,

    @ColumnInfo(name = "is_in_favorites")
    val isInFavorites: Boolean,

    @ColumnInfo(name = "vote_status")
    val voteStatus: Int,

    @ColumnInfo(name = "updatedAt")
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
