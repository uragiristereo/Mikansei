package com.uragiristereo.mikansei.core.database.post

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uragiristereo.mikansei.core.model.danbooru.Post
import java.time.OffsetDateTime

@Entity(tableName = "posts")
data class PostRow(
    @PrimaryKey
    @ColumnInfo(name = "post_id")
    val id: Int,

    @ColumnInfo(name = "data")
    val data: Post,

    @ColumnInfo(name = "uploader_name", defaultValue = "null")
    val uploaderName: String? = null,

    @ColumnInfo(name = "updated_at")
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
