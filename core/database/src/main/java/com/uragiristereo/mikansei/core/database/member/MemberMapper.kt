package com.uragiristereo.mikansei.core.database.member

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.database.entity.Member

fun MemberRow.toMember(): Member {
    return Member(
        id = id,
        name = name,
        level = Profile.Level.getLevelById(level),
    )
}

fun Member.toMemberRow(): MemberRow {
    return MemberRow(
        id = id,
        name = name,
        level = level.id,
    )
}
