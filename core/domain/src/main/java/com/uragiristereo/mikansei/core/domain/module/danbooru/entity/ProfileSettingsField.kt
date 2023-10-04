package com.uragiristereo.mikansei.core.domain.module.danbooru.entity

import com.uragiristereo.mikansei.core.model.preferences.user.DetailSizePreference

data class ProfileSettingsField(
    val enableSafeMode: Boolean? = null,
    val showDeletedPosts: Boolean? = null,
    val defaultImageSize: DetailSizePreference? = null,
    val blacklistedTags: List<String>? = null,
)
