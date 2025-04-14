package com.uragiristereo.mikansei.core.danbooru.model.wiki

import com.uragiristereo.mikansei.core.model.danbooru.DanbooruDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class DanbooruWikiResponse(
    @SerialName("id")
    val id: Int,

    @SerialName("created_at")
    @Serializable(DanbooruDateSerializer::class)
    val createdAt: OffsetDateTime,

    @SerialName("updated_at")
    @Serializable(DanbooruDateSerializer::class)
    val updatedAt: OffsetDateTime,

    @SerialName("title")
    val title: String,

    @SerialName("body")
    val body: String,

    @SerialName("is_locked")
    val isLocked: Boolean,

    @SerialName("is_deleted")
    val isDeleted: Boolean,

    @SerialName("other_names")
    val otherNames: List<String>,
)
