package com.uragiristereo.mikansei.core.database.dao.search_history

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "search_history")
data class SearchHistoryRow(
    @PrimaryKey val uuid: String,
    val tags: String,
    val date: Date,
)
