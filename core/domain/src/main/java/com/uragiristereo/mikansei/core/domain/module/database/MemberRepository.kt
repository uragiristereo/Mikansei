package com.uragiristereo.mikansei.core.domain.module.database

import com.uragiristereo.mikansei.core.domain.module.database.entity.Member
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    fun get(userId: Int): Flow<Member?>

    suspend fun update(member: Member)
}
