package com.uragiristereo.mikansei.core.danbooru.model.post

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DanbooruMediaAsset(
    @SerialName("variants")
    val variants: List<DanbooruVariant>?,
)
