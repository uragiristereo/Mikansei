package com.uragiristereo.mejiboard.core.booru.source.danbooru.model.post

import com.uragiristereo.mejiboard.core.booru.source.danbooru.model.DanbooruDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class DanbooruPost(
    val id: Int?,

    @SerialName(value = "created_at")
    @Serializable(with = DanbooruDateSerializer::class)
    val createdAt: Date,

    @SerialName(value = "uploader_id")
    val uploaderId: Int,
    val score: Int,
    val source: String,
    val md5: String? = null,

    @SerialName(value = "last_comment_bumped_at")
    val lastCommentBumpedAt: String?,
    val rating: String,

    @SerialName(value = "image_width")
    val imageWidth: Int,

    @SerialName(value = "image_height")
    val imageHeight: Int,

    @SerialName(value = "tag_string")
    val tagString: String,

    @SerialName(value = "fav_count")
    val favCount: Int,

    @SerialName(value = "file_ext")
    val fileExt: String,

    @SerialName(value = "last_noted_at")
    @Serializable(with = DanbooruDateSerializer::class)
    val lastNotedAt: Date?,

    @SerialName(value = "parent_id")
    val parentId: Int?,

    @SerialName(value = "has_children")
    val hasChildren: Boolean,

    @SerialName(value = "approver_id")
    val approverId: Int?,

    @SerialName(value = "tag_count_general")
    val tagCountGeneral: Int,

    @SerialName(value = "tag_count_artist")
    val tagCountArtist: Int,

    @SerialName(value = "tag_count_character")
    val tagCountCharacter: Int,

    @SerialName(value = "tag_count_copyright")
    val tagCountCopyright: Int,

    @SerialName(value = "file_size")
    val fileSize: Int,

    @SerialName(value = "up_score")
    val upScore: Int,

    @SerialName(value = "down_score")
    val downScore: Int,

    @SerialName(value = "is_pending")
    val isPending: Boolean,

    @SerialName(value = "is_flagged")
    val isFlagged: Boolean,

    @SerialName(value = "is_deleted")
    val isDeleted: Boolean,

    @SerialName(value = "tag_count")
    val tagCount: Int,

    @SerialName(value = "updated_at")
    val updatedAt: String,

    @SerialName(value = "is_banned")
    val isBanned: Boolean,

    @SerialName(value = "pixiv_id")
    val pixivId: Int?,

    @SerialName(value = "last_commented_at")
    val lastCommentedAt: String?,

    @SerialName(value = "has_active_children")
    val hasActiveChildren: Boolean,

    @SerialName(value = "bit_flags")
    val bitFlags: Int,

    @SerialName(value = "tag_count_meta")
    val tagCountMeta: Int,

    @SerialName(value = "has_large")
    val hasLarge: Boolean?,

    @SerialName(value = "has_visible_children")
    val hasVisibleChildren: Boolean,

    @SerialName(value = "tag_string_general")
    val tagStringGeneral: String,

    @SerialName(value = "tag_string_character")
    val tagStringCharacter: String,

    @SerialName(value = "tag_string_copyright")
    val tagStringCopyright: String,

    @SerialName(value = "tag_string_artist")
    val tagStringArtist: String,

    @SerialName(value = "tag_string_meta")
    val tagStringMeta: String,

    @SerialName(value = "file_url")
    val fileUrl: String? = null,

    @SerialName(value = "large_file_url")
    val largeFileUrl: String? = null,

    @SerialName(value = "preview_file_url")
    val previewFileUrl: String? = null,
)
