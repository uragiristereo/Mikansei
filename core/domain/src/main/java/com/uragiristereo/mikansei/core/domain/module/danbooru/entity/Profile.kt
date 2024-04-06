package com.uragiristereo.mikansei.core.domain.module.danbooru.entity

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
    enum class Level(val id: Int) {
        Unknown(id = -1),
        Lurker(id = 0),
        Restricted(id = 10),
        Member(id = 20),
        Gold(id = 30),
        Platinum(id = 31),
        Builder(id = 32),
        Contributor(id = 35),
        Approver(id = 37),
        Moderator(id = 40),
        Admin(id = 50),
        Owner(id = 60);

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
