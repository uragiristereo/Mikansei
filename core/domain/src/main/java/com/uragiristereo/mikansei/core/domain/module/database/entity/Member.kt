package com.uragiristereo.mikansei.core.domain.module.database.entity

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile

data class Member(
    val id: Int,
    val name: String,
    val level: Profile.Level,
)
