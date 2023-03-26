package com.uragiristereo.mikansei.core.model.danbooru.user

import com.uragiristereo.mikansei.core.model.preferences.DetailSizePreference

data class User(
    // Danbooru
    val id: Int,
    val name: String,
    val apiKey: String,
    val level: UserLevel,
    val safeMode: Boolean = true,
    val showDeletedPosts: Boolean = false,
    val defaultImageSize: DetailSizePreference = DetailSizePreference.COMPRESSED,
    val blacklistedTags: List<String>,

    // Mikansei
    val isActive: Boolean = false,
    val blurQuestionablePosts: Boolean = true,
    val blurExplicitPosts: Boolean = true,
    val nameAlias: String,
)
