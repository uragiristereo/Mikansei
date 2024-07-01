package com.uragiristereo.mikansei.core.danbooru.model.post.vote

import com.uragiristereo.mikansei.core.model.danbooru.DanbooruDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class DanbooruPostVote(
    @SerialName("id")
    val id: Int,

    @SerialName("post_id")
    val postId: Int,

    @SerialName("user_id")
    val userId: Int,

    @SerialName("created_at")
    @Serializable(DanbooruDateSerializer::class)
    val createdAt: OffsetDateTime,

    @SerialName("updated_at")
    @Serializable(DanbooruDateSerializer::class)
    val updatedAt: OffsetDateTime,

    @SerialName("score")
    val score: Int,

    @SerialName("is_deleted")
    val isDeleted: Boolean,
)
