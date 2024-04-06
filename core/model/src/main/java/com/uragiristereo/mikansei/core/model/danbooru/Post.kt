package com.uragiristereo.mikansei.core.model.danbooru

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable
import java.util.Date

@Stable
@Serializable
data class Post(
    val id: Int,

    @Serializable(DanbooruDateSerializer::class)
    val createdAt: Date,

    val uploaderId: Int,
    val source: String?,
    val rating: Rating,
    val status: Status,
    val relationshipType: RelationshipType,

    val score: Int,
    val upScore: Int,
    val downScore: Int,
    val favorites: Int,

    val type: Type,
    val medias: Medias,
    val aspectRatio: Float,

    val tags: List<String>,
) {
    @Serializable
    data class Medias(
        val original: Media,
        val scaled: Media?,
        val preview: Media,
    ) {
        val hasScaled = scaled != null
    }

    @Serializable
    data class Media(
        val url: String,
        val width: Int,
        val height: Int,
        val fileType: String,
    )

    enum class Type {
        IMAGE,
        ANIMATED_GIF,
        VIDEO,
        UGOIRA,
    }

    enum class Status {
        ACTIVE,
        PENDING,
        DELETED,
        BANNED,
    }

    enum class RelationshipType {
        NONE,
        PARENT,
        CHILD,
        PARENT_CHILD,
    }
}
