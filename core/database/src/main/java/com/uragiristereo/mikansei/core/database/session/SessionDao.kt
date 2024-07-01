package com.uragiristereo.mikansei.core.database.session

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("select * from sessions_v2 where session_id = :sessionId")
    fun get(sessionId: String): Flow<SessionRow?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(sessionRow: SessionRow)

    @Upsert
    suspend fun update(sessionRow: SessionRow)

    @Query("delete from sessions_v2 where session_id = :sessionId")
    suspend fun delete(sessionId: String)

    @Query("delete from sessions_v2")
    suspend fun reset()
}
