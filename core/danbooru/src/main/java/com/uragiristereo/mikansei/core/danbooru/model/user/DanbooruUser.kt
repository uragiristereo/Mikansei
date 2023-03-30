package com.uragiristereo.mikansei.core.danbooru.model.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DanbooruUser(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("level")
    val level: Int,

    @SerialName("inviter_id")
    val inviterId: Int?,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("post_update_count")
    val postUpdateCount: Int,

    @SerialName("note_update_count")
    val noteUpdateCount: Int,

    @SerialName("post_upload_count")
    val postUploadCount: Int,

    @SerialName("is_deleted")
    val isDeleted: Boolean,

    @SerialName("level_string")
    val levelString: String,

    @SerialName("is_banned")
    val isBanned: Boolean,

    @SerialName("wiki_page_version_count")
    val wikiPageVersionCount: Int,

    @SerialName("artist_version_count")
    val artistVersionCount: Int,

    @SerialName("artist_commentary_version_count")
    val artistCommentaryVersionCount: Int,

    @SerialName("pool_version_count")
    val poolVersionCount: Int,

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
