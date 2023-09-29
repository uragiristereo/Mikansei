package com.uragiristereo.mikansei.core.danbooru.model.post

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DanbooruVariant(
    @SerialName("type")
    val type: String,

    @SerialName("url")
    val url: String,

    @SerialName("width")
    val width: Int,

    @SerialName("height")
    val height: Int,

    @SerialName("file_ext")
    val fileExt: String,
)
