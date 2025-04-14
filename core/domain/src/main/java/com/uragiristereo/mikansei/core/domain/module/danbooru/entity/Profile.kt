package com.uragiristereo.mikansei.core.domain.module.danbooru.entity

import androidx.compose.ui.graphics.Color
import com.uragiristereo.mikansei.core.model.preferences.user.DetailSizePreference
import com.uragiristereo.mikansei.core.model.preferences.user.RatingPreference

data class Profile(
    val id: Int,
    val name: String,
    val apiKey: String = "",
    val level: Level,
    val danbooru: DanbooruSettings,
    val mikansei: MikanseiSettings = MikanseiSettings(),
) {
    enum class Level(
        val id: Int,
        val lightColor: Color,
        val darkColor: Color,
    ) {
        Unknown(
            id = -1,
            lightColor = Color(0xFF0075F8),
            darkColor = Color(0xFF009BE6),
        ),
        Lurker(
            id = 0,
            lightColor = Color(0xFF0075F8),
            darkColor = Color(0xFF009BE6),
        ),
        Restricted(
            id = 10,
            lightColor = Color(0xFF0075F8),
            darkColor = Color(0xFF009BE6),
        ),
        Member(
            id = 20,
            lightColor = Color(0xFF0075F8),
            darkColor = Color(0xFF009BE6),
        ),
        Gold(
            id = 30,
            lightColor = Color(0xFFFD9200),
            darkColor = Color(0xFFEAD084),
        ),
        Platinum(
            id = 31,
            lightColor = Color(0xFF777892),
            darkColor = Color(0xFFABABBC),
        ),
        Builder(
            id = 32,
            lightColor = Color(0xFFA800AA),
            darkColor = Color(0xFFC797FF),
        ),
        Contributor(
            id = 35,
            lightColor = Color(0xFFA800AA),
            darkColor = Color(0xFFC797FF),
        ),
        Approver(
            id = 37,
            lightColor = Color(0xFFA800AA),
            darkColor = Color(0xFFC797FF),
        ),
        Moderator(
            id = 40,
            lightColor = Color(0xFF00AB2C),
            darkColor = Color(0xFF35C64A),
        ),
        Admin(
            id = 50,
            lightColor = Color(0xFFED2426),
            darkColor = Color(0xFFFF8A8B),
        ),
        Owner(
            id = 60,
            lightColor = Color(0xFFED2426),
            darkColor = Color(0xFFFF8A8B),
        );

        companion object {
            fun getLevelById(id: Int): Level {
                return Level.entries.firstOrNull { it.id == id } ?: Unknown
            }
        }
    }

    data class DanbooruSettings(
        val safeMode: Boolean = true,
        val showDeletedPosts: Boolean = false,
        val defaultImageSize: DetailSizePreference = DetailSizePreference.COMPRESSED,
        val blacklistedTags: List<String>,
    )

    data class MikanseiSettings(
        val isActive: Boolean = false,
        val postsRatingFilter: RatingPreference = RatingPreference.GENERAL_ONLY,
        val blurQuestionablePosts: Boolean = true,
        val blurExplicitPosts: Boolean = true,
        val showPendingPosts: Boolean = false,
        val nameAlias: String = "",
    )

    fun isAnonymous(): Boolean {
        return id == 0
    }

    fun isNotAnonymous(): Boolean {
        return !isAnonymous()
    }
}
