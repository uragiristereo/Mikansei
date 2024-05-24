package com.uragiristereo.mikansei.core.domain.module.database

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val active: StateFlow<Profile>

    fun getAll(): Flow<List<Profile>>

    fun get(id: Int): Flow<Profile>

    suspend fun getOnce(id: Int): Profile?

    suspend fun isUserExists(id: Int): Boolean

    suspend fun switchActive(id: Int)

    suspend fun add(user: Profile)

    suspend fun update(user: Profile)

    suspend fun update(transformation: (Profile) -> Profile)

    suspend fun delete(user: Profile)
}
