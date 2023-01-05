package com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.post

import com.google.gson.annotations.SerializedName
import java.util.Date

data class GelbooruPost(
    val change: Int,

    @SerializedName(value = "created_at")
    val createdAt: Date,

    @SerializedName(value = "creator_id")
    val creatorId: Int,

    val directory: String,

    @SerializedName(value = "file_url")
    val fileUrl: String,

    @SerializedName(value = "has_children")
    val hasChildren: String,

    @SerializedName(value = "has_comments")
    val hasComments: String,

    @SerializedName(value = "has_notes")
    val hasNotes: String,

    val height: Int,
    val id: Int,
    val image: String,
    val md5: String,
    val owner: String,

    @SerializedName(value = "parent_id")
    val parentId: Int,

    @SerializedName(value = "post_locked")
    val postLocked: Int,

    @SerializedName(value = "preview_height")
    val previewHeight: Int,

    @SerializedName(value = "preview_url")
    val previewUrl: String,

    @SerializedName(value = "preview_width")
    val previewWidth: Int,

    val rating: String,
    val sample: Int,

    @SerializedName(value = "sample_height")
    val sampleHeight: Int,

    @SerializedName(value = "sample_width")
    val sampleWidth: Int,

    val score: Int,
    val source: Any,
    val tags: String,
    val title: Any,
    val width: Int,
)
