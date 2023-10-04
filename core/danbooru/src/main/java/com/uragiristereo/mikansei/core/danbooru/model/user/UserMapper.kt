package com.uragiristereo.mikansei.core.danbooru.model.user

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.User

fun DanbooruUser.toUser(): User {
    return User(
        id = id,
        name = name,
        level = Profile.Level.getLevelById(level),
    )
}
