package com.uragiristereo.mejiboard.core.booru.source.yandere.model.post

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YanderePost(
    val id: Int,
    val rating: String,
    val tags: String,
    @SerialName("created_at") val createdAt: Long,
    val author: String,
    val source: String,

    @SerialName("file_url") val fileUrl: String,
    val width: Int,
    val height: Int,

    @SerialName("sample_url") val sampleUrl: String,
    @SerialName("sample_width") val sampleWidth: Int,
    @SerialName("sample_height") val sampleHeight: Int,

    @SerialName("preview_url") val previewUrl: String,
    @SerialName("preview_width") val previewWidth: Int,
    @SerialName("preview_height") val previewHeight: Int,
)
