package com.uragiristereo.mikansei.core.database.member

import com.uragiristereo.mikansei.core.domain.module.database.MemberRepository
import com.uragiristereo.mikansei.core.domain.module.database.entity.Member
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MemberRepositoryImpl(private val memberDao: MemberDao) : MemberRepository {
    override fun get(userId: Int): Flow<Member?> {
        return memberDao.get(userId).map {
            it?.toMember()
        }
    }

    override suspend fun update(member: Member) {
        memberDao.update(member.toMemberRow())
    }
}
