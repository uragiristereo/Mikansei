package com.uragiristereo.mikansei.core.database.user_delegation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_delegations")
data class UserDelegationRow(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "delegated_user_id")
    val delegatedUserId: Int?,
)
