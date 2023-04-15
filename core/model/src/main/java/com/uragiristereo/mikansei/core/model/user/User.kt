package com.uragiristereo.mikansei.core.model.user

import com.uragiristereo.mikansei.core.model.danbooru.UserLevel
import com.uragiristereo.mikansei.core.model.user.preference.DetailSizePreference
import com.uragiristereo.mikansei.core.model.user.preference.RatingPreference

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
    val postsRatingFilter: RatingPreference = RatingPreference.GENERAL_ONLY,
    val blurQuestionablePosts: Boolean = true,
    val blurExplicitPosts: Boolean = true,
    val nameAlias: String,
)

fun User.isAnonymous(): Boolean = id == 0

fun User.isNotAnonymous(): Boolean = id != 0
