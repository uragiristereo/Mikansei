package com.uragiristereo.mikansei.core.domain.module.database

import kotlinx.coroutines.flow.Flow

interface UserDelegationRepository {
    fun getDelegatedUserById(userId: Int): Flow<Int?>

    suspend fun setDelegation(userId: Int, delegatedUserId: Int?)

    suspend fun removeDelegation(userId: Int)
}
