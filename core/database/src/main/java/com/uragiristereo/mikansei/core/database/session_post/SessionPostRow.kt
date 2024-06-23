package com.uragiristereo.mikansei.core.database.session_post

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.uragiristereo.mikansei.core.database.post.PostRow
import com.uragiristereo.mikansei.core.database.session.SessionRow
import java.util.UUID

@Entity(
    tableName = "session_posts",
    foreignKeys = [
        ForeignKey(
            entity = PostRow::class,
            childColumns = ["post_id"],
            parentColumns = ["post_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = SessionRow::class,
            childColumns = ["session_id"],
            parentColumns = ["session_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class SessionPostRow(
    @PrimaryKey
    @ColumnInfo(name = "session_post_id")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "session_id", index = true)
    val sessionId: String,

    @ColumnInfo(name = "sequence")
    val sequence: Int,

    @ColumnInfo(name = "post_id", index = true)
    val postId: Int,
)
