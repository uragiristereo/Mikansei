package com.uragiristereo.mikansei.feature.user.manage.core

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile

data class UsersState(
    val active: Profile,
    val inactive: List<Profile>,
)
