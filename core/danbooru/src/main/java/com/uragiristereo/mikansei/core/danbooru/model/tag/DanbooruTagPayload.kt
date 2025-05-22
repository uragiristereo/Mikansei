package com.uragiristereo.mikansei.core.danbooru.model.tag

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DanbooruTagPayload(
    @SerialName("search")
    val search: Search,
) {
    @Serializable
    data class Search(
        @SerialName("name")
        val name: List<String>,
    )

    companion object {
        fun create(tags: List<String>): DanbooruTagPayload {
            return DanbooruTagPayload(
                Search(name = tags)
            )
        }
    }
}
