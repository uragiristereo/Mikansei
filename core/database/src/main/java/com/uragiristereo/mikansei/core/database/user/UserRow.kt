package com.uragiristereo.mikansei.core.database.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uragiristereo.mikansei.core.model.preferences.user.RatingPreference

@Entity(tableName = "users")
data class UserRow(
    // Danbooru
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "api_key", defaultValue = "")
    val apiKey: String,

    @ColumnInfo(name = "level")
    val level: Int,

    @ColumnInfo(name = "safe_mode", defaultValue = "1")
    val safeMode: Boolean = true,

    @ColumnInfo(name = "show_deleted_posts", defaultValue = "0")
    val showDeletedPosts: Boolean = false,

    @ColumnInfo(name = "default_image_size", defaultValue = "large")
    val defaultImageSize: String = "large",

    @ColumnInfo(name = "blacklisted_tags", defaultValue = "guro\nscat\nfurry")
    val blacklistedTags: String = "guro\nscat\nfurry",

    // Mikansei
    @ColumnInfo(name = "is_active", defaultValue = "false")
    val isActive: Boolean = false,

    @ColumnInfo(name = "posts_rating_filter", defaultValue = "GENERAL_ONLY")
    val postsRatingFilter: RatingPreference = RatingPreference.GENERAL_ONLY,

    @ColumnInfo(name = "blur_questionable_posts", defaultValue = "1")
    val blurQuestionablePosts: Boolean = true,

    @ColumnInfo(name = "blur_explicit_posts", defaultValue = "1")
    val blurExplicitPosts: Boolean = true,

    @ColumnInfo(name = "show_pending_posts")
    val showPendingPosts: Boolean,
)
