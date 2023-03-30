package com.uragiristereo.mikansei.core.danbooru.model.user.field

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class DanbooruUserFieldData(
    @SerialName("enable_safe_mode")
    val enableSafeMode: Boolean? = null,

    @SerialName("show_deleted_posts")
    val showDeletedPosts: Boolean? = null,

    @SerialName("default_image_size")
    val defaultImageSize: String? = null,

    @SerialName("blacklisted_tags")
    val blacklistedTags: String? = null,
)
