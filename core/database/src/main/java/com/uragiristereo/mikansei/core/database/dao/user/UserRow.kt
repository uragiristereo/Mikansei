package com.uragiristereo.mikansei.core.database.dao.user

import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val blacklistedTags: String = "guro scat furry",

    // Mikansei
    val isActive: Boolean = false,
    val blurQuestionablePosts: Boolean = true,
    val blurExplicitPosts: Boolean = true,
)
