package com.uragiristereo.mikansei.core.database.user_delegation

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDelegationDao {
    @Query("select delegated_user_id from user_delegations where user_id = :userId")
    fun getDelegatedUserId(userId: Int): Flow<Int?>

    @Upsert
    suspend fun set(userDelegationRow: UserDelegationRow)

    @Delete
    suspend fun delete(userDelegationRow: UserDelegationRow)
}
