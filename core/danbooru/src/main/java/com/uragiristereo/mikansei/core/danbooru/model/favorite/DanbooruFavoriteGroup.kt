package com.uragiristereo.mikansei.core.danbooru.model.favorite


import androidx.annotation.Keep
import com.uragiristereo.mikansei.core.model.danbooru.DanbooruDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Keep
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
    val createdAt: Date,

    @SerialName("updated_at")
    @Serializable(DanbooruDateSerializer::class)
    val updatedAt: Date,

    @SerialName("is_public")
    val isPublic: Boolean,
)
