package com.uragiristereo.mikansei.core.domain.entity.user

import com.uragiristereo.mikansei.core.model.user.preference.DetailSizePreference

data class UserField(
    val safeMode: Boolean? = null,
    val showDeletedPosts: Boolean? = null,
    val defaultImageSize: DetailSizePreference? = null,
    val blacklistedTags: List<String>? = null,
)
