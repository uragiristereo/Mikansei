package com.uragiristereo.mikansei.core.database.session_post

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.uragiristereo.mikansei.core.model.danbooru.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionPostDao {
    @Query(
        """
            select posts.data from posts
            inner join session_posts on session_posts.post_id = posts.post_id
            where session_posts.session_id = :sessionId
            order by sequence asc
        """
    )
    fun getPosts(sessionId: String): Flow<List<Post>>

    @Insert
    suspend fun add(sessions: List<SessionPostRow>)

    @Query("delete from session_posts where session_id = :sessionId")
    suspend fun deleteBySessionId(sessionId: String)

    @Transaction
    suspend fun update(sessionId: String, postIds: List<Int>) {
        deleteBySessionId(sessionId)

        val session = postIds
            .distinct()
            .mapIndexed { index, postId ->
                SessionPostRow(
                    sessionId = sessionId,
                    postId = postId,
                    sequence = index,
                )
            }

        add(session)
    }

    @Query("delete from session_posts")
    suspend fun reset()
}
