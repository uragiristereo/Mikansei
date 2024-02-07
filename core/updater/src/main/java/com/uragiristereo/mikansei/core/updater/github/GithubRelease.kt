package com.uragiristereo.mikansei.core.updater.github

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GithubRelease(
    @SerialName("name")
    val name: String,

    @SerialName("body")
    val body: String,

    @SerialName("html_url")
    val htmlUrl: String,

    @SerialName("draft")
    val draft: Boolean,
)
