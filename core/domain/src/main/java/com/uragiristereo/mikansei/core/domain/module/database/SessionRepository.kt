package com.uragiristereo.mikansei.core.domain.module.database

import com.uragiristereo.mikansei.core.domain.module.database.entity.Session
import com.uragiristereo.mikansei.core.model.danbooru.Post
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getSession(sessionId: String): Flow<Session?>

    suspend fun addSession(session: Session)

    suspend fun updateSession(session: Session)

    fun getPosts(sessionId: String): Flow<List<Post>>

    suspend fun updatePosts(sessionId: String, posts: List<Post>)

    suspend fun delete(sessionId: String)

    suspend fun reset()
}
