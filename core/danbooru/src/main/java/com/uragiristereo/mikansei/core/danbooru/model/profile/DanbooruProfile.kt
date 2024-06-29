package com.uragiristereo.mikansei.core.danbooru.model.profile

import com.uragiristereo.mikansei.core.model.danbooru.DanbooruDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class DanbooruProfile(
    @SerialName("last_logged_in_at")
    @Serializable(DanbooruDateSerializer::class)
    val lastLoggedInAt: OffsetDateTime,

    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("level")
    val level: Int,

    @SerialName("inviter_id")
    val inviterId: Int?,

    @SerialName("created_at")

    @Serializable(DanbooruDateSerializer::class)
    val createdAt: OffsetDateTime,

    @SerialName("last_forum_read_at")

    @Serializable(DanbooruDateSerializer::class)
    val lastForumReadAt: OffsetDateTime,

    @SerialName("comment_threshold")
    val commentThreshold: Int,

    @SerialName("updated_at")
    @Serializable(DanbooruDateSerializer::class)
    val updatedAt: OffsetDateTime,

    @SerialName("default_image_size")
    val defaultImageSize: String,

    @SerialName("favorite_tags")
    val favoriteTags: String?,

    @SerialName("blacklisted_tags")
    val blacklistedTags: String,

    @SerialName("time_zone")
    val timeZone: String,

    @SerialName("post_update_count")
    val postUpdateCount: Int,

    @SerialName("note_update_count")
    val noteUpdateCount: Int,

    @SerialName("favorite_count")
    val favoriteCount: Int,

    @SerialName("post_upload_count")
    val postUploadCount: Int,

    @SerialName("per_page")
    val perPage: Int,

    @SerialName("custom_style")
    val customStyle: String?,

    @SerialName("theme")
    val theme: String,

    @SerialName("is_deleted")
    val isDeleted: Boolean,

    @SerialName("level_string")
    val levelString: String,

    @SerialName("is_banned")
    val isBanned: Boolean,

    @SerialName("receive_email_notifications")
    val receiveEmailNotifications: Boolean,

    @SerialName("new_post_navigation_layout")
    val newPostNavigationLayout: Boolean,

    @SerialName("enable_private_favorites")
    val enablePrivateFavorites: Boolean,

    @SerialName("show_deleted_children")
    val showDeletedChildren: Boolean,

    @SerialName("disable_categorized_saved_searches")
    val disableCategorizedSavedSearches: Boolean,

    @SerialName("disable_tagged_filenames")
    val disableTaggedFilenames: Boolean,

    @SerialName("disable_mobile_gestures")
    val disableMobileGestures: Boolean,

    @SerialName("enable_safe_mode")
    val enableSafeMode: Boolean,

    @SerialName("enable_desktop_mode")
    val enableDesktopMode: Boolean,

    @SerialName("disable_post_tooltips")
    val disablePostTooltips: Boolean,

    @SerialName("requires_verification")
    val requiresVerification: Boolean,

    @SerialName("is_verified")
    val isVerified: Boolean,

    @SerialName("show_deleted_posts")
    val showDeletedPosts: Boolean,

    @SerialName("statement_timeout")
    val statementTimeout: Int,

    @SerialName("favorite_group_limit")
    val favoriteGroupLimit: Int?,

    @SerialName("tag_query_limit")
    val tagQueryLimit: Int?,

    @SerialName("max_saved_searches")
    val maxSavedSearches: Int,

    @SerialName("wiki_page_version_count")
    val wikiPageVersionCount: Int,

    @SerialName("artist_version_count")
    val artistVersionCount: Int,

    @SerialName("artist_commentary_version_count")
    val artistCommentaryVersionCount: Int,

    @SerialName("pool_version_count")
    val poolVersionCount: Int?,

    @SerialName("forum_post_count")
    val forumPostCount: Int,

    @SerialName("comment_count")
    val commentCount: Int,

    @SerialName("favorite_group_count")
    val favoriteGroupCount: Int,

    @SerialName("appeal_count")
    val appealCount: Int,

    @SerialName("flag_count")
    val flagCount: Int,

    @SerialName("positive_feedback_count")
    val positiveFeedbackCount: Int,

    @SerialName("neutral_feedback_count")
    val neutralFeedbackCount: Int,

    @SerialName("negative_feedback_count")
    val negativeFeedbackCount: Int,
)
