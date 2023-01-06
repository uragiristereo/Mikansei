package com.uragiristereo.mejiboard.core.booru.source.danbooru.model.tag

import com.google.gson.annotations.SerializedName
import java.util.Date

data class DanbooruTag(
    val id: Int,
    val name: String,

    @SerializedName(value = "post_count")
    val postCount: Int,

    val category: Int,

    @SerializedName(value = "created_at")
    val createdAt: Date,

    @SerializedName(value = "updated_at")
    val updatedAt: Date,

    @SerializedName(value = "is_locked")
    val isLocked: Boolean,

    @SerializedName(value = "is_deprecated")
    val isDeprecated: Boolean,
)
