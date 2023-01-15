package com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.post

import com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.serializer.GelbooruAnySerializer
import com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.serializer.GelbooruDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class GelbooruPost(
    val change: Int,

    @SerialName(value = "created_at")
    @Serializable(with = GelbooruDateSerializer::class)
    val createdAt: Date,

    @SerialName(value = "creator_id")
    val creatorId: Int,

    val directory: String,

    @SerialName(value = "file_url")
    val fileUrl: String,

    @SerialName(value = "has_children")
    val hasChildren: String,

    @SerialName(value = "has_comments")
    val hasComments: String,

    @SerialName(value = "has_notes")
    val hasNotes: String,

    val height: Int,
    val id: Int,
    val image: String,
    val md5: String,
    val owner: String,

    @SerialName(value = "parent_id")
    val parentId: Int,

    @SerialName(value = "post_locked")
    val postLocked: Int,

    @SerialName(value = "preview_height")
    val previewHeight: Int,

    @SerialName(value = "preview_width")
    val previewWidth: Int,

    val rating: String,
    val sample: Int,

    @SerialName(value = "sample_height")
    val sampleHeight: Int,

    @SerialName(value = "sample_width")
    val sampleWidth: Int,

    val score: Int,

    @Serializable(with = GelbooruAnySerializer::class)
    val source: Any,

    val tags: String,

    @Serializable(with = GelbooruAnySerializer::class)
    val title: Any,

    val width: Int,
)
