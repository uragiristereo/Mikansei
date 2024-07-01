package com.uragiristereo.mikansei.core.danbooru.model.favorite


import com.uragiristereo.mikansei.core.model.danbooru.DanbooruDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class DanbooruFavoriteGroup(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("creator_id")
    val creatorId: Int,

    @SerialName("post_ids")
    val postIds: List<Int>,

    @SerialName("created_at")
    @Serializable(DanbooruDateSerializer::class)
    val createdAt: OffsetDateTime,

    @SerialName("updated_at")
    @Serializable(DanbooruDateSerializer::class)
    val updatedAt: OffsetDateTime,

    @SerialName("is_public")
    val isPublic: Boolean,
)
