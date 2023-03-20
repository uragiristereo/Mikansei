package com.uragiristereo.mejiboard.core.database.dao.session

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SessionDao {
    @Query("select * from sessions where sessionUuid = :sessionUuid order by sequence asc")
    suspend fun get(sessionUuid: String): List<SessionRow>

    @Insert
    suspend fun insert(sessions: List<SessionRow>)

    @Query("delete from sessions where sessionUuid = :sessionUuid")
    suspend fun delete(sessionUuid: String)

    @Query("delete from sessions")
    suspend fun reset()
}
