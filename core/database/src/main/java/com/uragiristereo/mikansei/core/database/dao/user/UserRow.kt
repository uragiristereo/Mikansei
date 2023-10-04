package com.uragiristereo.mikansei.core.database.dao.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uragiristereo.mikansei.core.model.preferences.user.RatingPreference

@Entity(tableName = "users")
data class UserRow(
    // Danbooru
    @PrimaryKey val id: Int,
    val name: String,
    val apiKey: String,
    val level: Int,
    val safeMode: Boolean = true,
    val showDeletedPosts: Boolean = false,
    val defaultImageSize: String = "large",
    val blacklistedTags: String = "guro\nscat\nfurry",

    // Mikansei
    val isActive: Boolean = false,
    val postsRatingFilter: RatingPreference = RatingPreference.GENERAL_ONLY,
    val blurQuestionablePosts: Boolean = true,
    val blurExplicitPosts: Boolean = true,
)
