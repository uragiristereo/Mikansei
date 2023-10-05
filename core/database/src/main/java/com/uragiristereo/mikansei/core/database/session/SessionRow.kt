package com.uragiristereo.mikansei.core.database.session

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uragiristereo.mikansei.core.model.danbooru.Post

@Entity(tableName = "sessions")
data class SessionRow(
    @PrimaryKey
    val uuid: String,

    val sessionUuid: String,
    val sequence: Int,

    val post: Post,
)
