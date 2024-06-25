package com.uragiristereo.mikansei.core.database.tag_category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tag_categories")
data class TagCategoryRow(
    @PrimaryKey
    @ColumnInfo(name = "tag")
    val tag: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date(),
)
