package com.uragiristereo.mikansei.core.database.session

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions_v2")
data class SessionRow(
    @PrimaryKey
    @ColumnInfo(name = "session_id")
    val id: String,

    @ColumnInfo(name = "tags")
    val tags: String,

    @ColumnInfo(name = "scroll_index", defaultValue = "0")
    val scrollIndex: Int = 0,

    @ColumnInfo(name = "scroll_offset", defaultValue = "0")
    val scrollOffset: Int = 0,
)
