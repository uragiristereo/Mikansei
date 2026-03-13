package com.uragiristereo.mikansei.core.database.member

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("select * from members where user_id = :userId")
    fun get(userId: Int): Flow<MemberRow?>

    @Upsert
    suspend fun update(member: MemberRow)
}
