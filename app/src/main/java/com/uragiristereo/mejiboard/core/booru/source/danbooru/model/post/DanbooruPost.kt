package com.uragiristereo.mejiboard.core.booru.source.danbooru.model.post

import com.google.gson.annotations.SerializedName
import java.util.Date

data class DanbooruPost(
    val id: Int?,

    @SerializedName(value = "created_at")
    val createdAt: Date,

    @SerializedName(value = "uploader_id")
    val uploaderId: Int,
    val score: Int,
    val source: String,
    val md5: String?,

    @SerializedName(value = "last_comment_bumped_at")
    val lastCommentBumpedAt: String?,
    val rating: String,

    @SerializedName(value = "image_width")
    val imageWidth: Int,

    @SerializedName(value = "image_height")
    val imageHeight: Int,

    @SerializedName(value = "tag_string")
    val tagString: String,

    @SerializedName(value = "fav_count")
    val favCount: Int,

    @SerializedName(value = "file_ext")
    val fileExt: String,

    @SerializedName(value = "last_noted_at")
    val lastNotedAt: Date?,

    @SerializedName(value = "parent_id")
    val parentId: String?,

    @SerializedName(value = "has_children")
    val hasChildren: Boolean,

    @SerializedName(value = "approver_id")
    val approverId: String?,

    @SerializedName(value = "tag_count_general")
    val tagCountGeneral: Int,

    @SerializedName(value = "tag_count_artist")
    val tagCountArtist: Int,

    @SerializedName(value = "tag_count_character")
    val tagCountCharacter: Int,

    @SerializedName(value = "tag_count_copyright")
    val tagCountCopyright: Int,

    @SerializedName(value = "file_size")
    val fileSize: Int,

    @SerializedName(value = "up_score")
    val upScore: Int,

    @SerializedName(value = "down_score")
    val downScore: Int,

    @SerializedName(value = "is_pending")
    val isPending: Boolean,

    @SerializedName(value = "is_flagged")
    val isFlagged: Boolean,

    @SerializedName(value = "is_deleted")
    val isDeleted: Boolean,

    @SerializedName(value = "tag_count")
    val tagCount: Int,

    @SerializedName(value = "updated_at")
    val updatedAt: String,

    @SerializedName(value = "is_banned")
    val isBanned: Boolean,

    @SerializedName(value = "pixiv_id")
    val pixivId: Int?,

    @SerializedName(value = "last_commented_at")
    val lastCommentedAt: String?,

    @SerializedName(value = "has_active_children")
    val hasActiveChildren: Boolean,

    @SerializedName(value = "bit_flags")
    val bitFlags: Int,

    @SerializedName(value = "tag_count_meta")
    val tagCountMeta: Int,

    @SerializedName(value = "has_large")
    val hasLarge: Boolean?,

    @SerializedName(value = "has_visible_children")
    val hasVisibleChildren: Boolean,

    @SerializedName(value = "tag_string_general")
    val tagStringGeneral: String,

    @SerializedName(value = "tag_string_character")
    val tagStringCharacter: String,

    @SerializedName(value = "tag_string_copyright")
    val tagStringCopyright: String,

    @SerializedName(value = "tag_string_artist")
    val tagStringArtist: String,

    @SerializedName(value = "tag_string_meta")
    val tagStringMeta: String,

    @SerializedName(value = "file_url")
    val fileUrl: String?,

    @SerializedName(value = "large_file_url")
    val largeFileUrl: String?,

    @SerializedName(value = "preview_file_url")
    val previewFileUrl: String?,
)
