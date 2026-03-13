package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.database.MemberRepository
import com.uragiristereo.mikansei.core.domain.module.database.entity.Member
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess

class GetMemberUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(userId: Int): Result<Member> {
        val result = danbooruRepository.getUser(userId).mapSuccess {
            Member(
                id = it.id,
                name = it.name,
                level = it.level,
            )
        }

        if (result is Result.Success) {
            memberRepository.update(result.data)
        }

        return result
    }
}
