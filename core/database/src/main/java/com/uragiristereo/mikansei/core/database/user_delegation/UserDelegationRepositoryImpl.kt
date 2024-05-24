package com.uragiristereo.mikansei.core.database.user_delegation

import com.uragiristereo.mikansei.core.domain.module.database.UserDelegationRepository
import kotlinx.coroutines.flow.Flow

class UserDelegationRepositoryImpl(
    private val userDelegationDao: UserDelegationDao,
) : UserDelegationRepository {
    override fun getDelegatedUserById(userId: Int): Flow<Int?> {
        return userDelegationDao.getDelegatedUserId(userId)
    }

    override suspend fun setDelegation(userId: Int, delegatedUserId: Int?) {
        userDelegationDao.set(UserDelegationRow(userId, delegatedUserId))
    }

    override suspend fun removeDelegation(userId: Int) {
        userDelegationDao.delete(UserDelegationRow(userId, null))
    }
}
