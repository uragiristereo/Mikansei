package com.uragiristereo.mejiboard.data.database.session

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SessionDao {
    @Query(value = "SELECT * FROM sessions WHERE sessionId = :sessionId ORDER BY sequence ASC")
    fun getPosts(sessionId: String): List<PostSession>

    @Insert
    fun insertSession(posts: List<PostSession>)

    @Query(value = "DELETE FROM sessions WHERE sessionId = :sessionId")
    fun deleteSession(sessionId: String)

    @Query(value = "DELETE FROM sessions")
    fun deleteAllSession()
}
