package com.uragiristereo.mikansei.core.database.search_history

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity(tableName = "search_history")
data class SearchHistoryRow(
    @PrimaryKey val uuid: String,
    val tags: String,
    val date: OffsetDateTime,
)
