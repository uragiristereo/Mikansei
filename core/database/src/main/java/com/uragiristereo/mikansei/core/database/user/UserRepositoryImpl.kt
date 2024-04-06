package com.uragiristereo.mikansei.core.database.user

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

class UserRepositoryImpl(
    coroutineScope: CoroutineScope,
    private val userDao: UserDao,
) : UserRepository {
    override val active = userDao.getActive()
        .map {
            it.toProfile()
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = runBlocking {
                userDao.getActive().first().toProfile()
            },
        )

    override fun getAll() = userDao.getAll().map {
        it.toProfileList()
    }

    override fun get(id: Int) = userDao.get(id).map {
        it.toProfile()
    }

    override suspend fun isUserExists(id: Int) = userDao.isUserExists(id)

    override suspend fun switchActive(id: Int) = userDao.switchActiveUser(id)

    override suspend fun add(user: Profile) = userDao.add(user.toUserRow())

    override suspend fun update(user: Profile) = userDao.update(user.toUserRow())

    override suspend fun update(transformation: (Profile) -> Profile) {
        userDao.update(transformation(active.value).toUserRow())
    }

    override suspend fun delete(user: Profile) = userDao.delete(user.toUserRow())
}
