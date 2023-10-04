package com.uragiristereo.mikansei.core.danbooru.model.profile

import com.uragiristereo.mikansei.core.danbooru.model.user.DanbooruProfile
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.model.preferences.user.DetailSizePreference
import com.uragiristereo.mikansei.core.model.preferences.user.getEnumFromDanbooru

fun DanbooruProfile.toProfile(): Profile {
    return Profile(
        id = id,
        name = name,
        level = Profile.Level.getLevelById(level),
        danbooru = Profile.DanbooruSettings(
            safeMode = enableSafeMode,
            showDeletedPosts = showDeletedPosts,
            defaultImageSize = DetailSizePreference.entries.getEnumFromDanbooru(defaultImageSize),
            blacklistedTags = when {
                blacklistedTags.isNotBlank() -> blacklistedTags.split(' ').distinct()
                else -> listOf()
            },
        ),
    )
}
