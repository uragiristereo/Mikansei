package com.uragiristereo.mikansei.core.danbooru.model.saved_search

import com.uragiristereo.mikansei.core.model.danbooru.DanbooruDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class DanbooruSavedSearch(
    @SerialName("id")
    val id: Int,

    @SerialName("user_id")
    val userId: Int,

    @SerialName("query")
    val query: String,

    @SerialName("created_at")
    @Serializable(DanbooruDateSerializer::class)
    val createdAt: OffsetDateTime,

    @SerialName("updated_at")
    @Serializable(DanbooruDateSerializer::class)
    val updatedAt: OffsetDateTime,

    @SerialName("labels")
    val labels: List<String>,
)
