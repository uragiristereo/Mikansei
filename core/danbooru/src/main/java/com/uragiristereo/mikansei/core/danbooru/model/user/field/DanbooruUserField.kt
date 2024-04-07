package com.uragiristereo.mikansei.core.danbooru.model.user.field

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DanbooruUserField(
    @SerialName("user")
    val user: DanbooruUserFieldData,
)
