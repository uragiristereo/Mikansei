package com.uragiristereo.mikansei.core.database.session

import com.uragiristereo.mikansei.core.database.session_post.SessionPostDao
import com.uragiristereo.mikansei.core.domain.module.database.SessionRepository
import com.uragiristereo.mikansei.core.domain.module.database.entity.Session
import com.uragiristereo.mikansei.core.model.danbooru.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SessionRepositoryImpl(
    private val sessionDao: SessionDao,
    private val sessionPostDao: SessionPostDao,
) : SessionRepository {
    override fun getSession(sessionId: String): Flow<Session?> {
        return sessionDao.get(sessionId).map {
            it?.toSession()
        }
    }

    override suspend fun addSession(session: Session) {
        sessionDao.add(session.toSessionRow())
    }

    override suspend fun updateSession(session: Session) {
        sessionDao.update(session.toSessionRow())
    }

    override fun getPosts(sessionId: String): Flow<List<Post>> {
        return sessionPostDao.getPosts(sessionId)
    }

    override suspend fun updatePosts(sessionId: String, posts: List<Post>) {
        sessionPostDao.update(
            sessionId = sessionId,
            postIds = posts.map { it.id },
        )
    }

    override suspend fun delete(sessionId: String) {
        sessionDao.delete(sessionId)
    }

    override suspend fun reset() {
        sessionDao.reset()
        sessionPostDao.reset()
    }
}
