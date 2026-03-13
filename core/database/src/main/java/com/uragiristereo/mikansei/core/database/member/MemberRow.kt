package com.uragiristereo.mikansei.core.database.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class MemberRow(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "level")
    val level: Int,
)
