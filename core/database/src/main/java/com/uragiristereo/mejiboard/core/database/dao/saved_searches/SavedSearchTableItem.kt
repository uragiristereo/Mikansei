package com.uragiristereo.mejiboard.core.database.dao.saved_searches

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "saved_searches")
data class SavedSearchTableItem(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),

    val tag: String,
)
