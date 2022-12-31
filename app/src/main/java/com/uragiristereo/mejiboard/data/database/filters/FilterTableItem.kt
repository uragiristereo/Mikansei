package com.uragiristereo.mejiboard.data.database.filters

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "filters")
data class FilterTableItem(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),

    val tag: String,
    val enabled: Boolean = true,
)
