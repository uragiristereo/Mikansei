package com.uragiristereo.mikansei.core.updater.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Release(
    @SerialName("version")
    val version: Int,

    @SerialName("name")
    val name: String,

    @SerialName("url")
    val url: String,

    @SerialName("force_update")
    val forceUpdate: Boolean,

    @SerialName("description")
    val description: String,
)
