package com.uragiristereo.mikansei.core.danbooru.model.user.field

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class DanbooruUserField(
    @SerialName("user")
    val user: DanbooruUserFieldData,
)
