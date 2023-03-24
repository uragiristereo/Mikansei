package com.uragiristereo.mikansei.core.danbooru.model.post


import androidx.annotation.Keep
import com.uragiristereo.mikansei.core.model.danbooru.DanbooruDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Keep
@Serializable
data class DanbooruPost(
    @SerialName("id")
    val id: Int? = null,

    @SerialName("created_at")
    @Serializable(with = DanbooruDateSerializer::class)
    val createdAt: Date,

    @SerialName("uploader_id")
    val uploaderId: Int,

    @SerialName("score")
    val score: Int,

    @SerialName("source")
    val source: String? = null,

    @SerialName("md5")
    val md5: String? = null,

    @SerialName("last_comment_bumped_at")
    val lastCommentBumpedAt: String? = null, // TODO: unresolved

    @SerialName("rating")
    val rating: String,

    @SerialName("image_width")
    val imageWidth: Int,

    @SerialName("image_height")
    val imageHeight: Int,

    @SerialName("tag_string")
    val tagString: String,

    @SerialName("fav_count")
    val favCount: Int,

    @SerialName("file_ext")
    val fileExt: String,

    @SerialName("last_noted_at")
    val lastNotedAt: String? = null,  // TODO: unresolved

    @SerialName("parent_id")
    val parentId: Int?,

    @SerialName("has_children")
    val hasChildren: Boolean,

    @SerialName("approver_id")
    val approverId: Int? = null,

    @SerialName("tag_count_general")
    val tagCountGeneral: Int,

    @SerialName("tag_count_artist")
    val tagCountArtist: Int,

    @SerialName("tag_count_character")
    val tagCountCharacter: Int,

    @SerialName("tag_count_copyright")
    val tagCountCopyright: Int,

    @SerialName("file_size")
    val fileSize: Int,

    @SerialName("up_score")
    val upScore: Int,

    @SerialName("down_score")
    val downScore: Int,

    @SerialName("is_pending")
    val isPending: Boolean,

    @SerialName("is_flagged")
    val isFlagged: Boolean,

    @SerialName("is_deleted")
    val isDeleted: Boolean,

    @SerialName("tag_count")
    val tagCount: Int,

    @SerialName("updated_at")
    @Serializable(with = DanbooruDateSerializer::class)
    val updatedAt: Date,

    @SerialName("is_banned")
    val isBanned: Boolean,

    @SerialName("pixiv_id")
    val pixivId: Int?,

    @SerialName("last_commented_at")
    val lastCommentedAt: String? = null,  // TODO: unresolved

    @SerialName("has_active_children")
    val hasActiveChildren: Boolean,

    @SerialName("bit_flags")
    val bitFlags: Int,

    @SerialName("tag_count_meta")
    val tagCountMeta: Int,

    @SerialName("has_large")
    val hasLarge: Boolean,

    @SerialName("has_visible_children")
    val hasVisibleChildren: Boolean,

    @SerialName("tag_string_general")
    val tagStringGeneral: String,

    @SerialName("tag_string_character")
    val tagStringCharacter: String,

    @SerialName("tag_string_copyright")
    val tagStringCopyright: String,

    @SerialName("tag_string_artist")
    val tagStringArtist: String,

    @SerialName("tag_string_meta")
    val tagStringMeta: String,

    @SerialName("file_url")
    val fileUrl: String? = null,

    @SerialName("large_file_url")
    val largeFileUrl: String? = null,

    @SerialName("preview_file_url")
    val previewFileUrl: String? = null,
)
