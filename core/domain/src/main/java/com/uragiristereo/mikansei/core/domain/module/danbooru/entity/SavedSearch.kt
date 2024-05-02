package com.uragiristereo.mikansei.core.domain.module.danbooru.entity

import kotlinx.serialization.Serializable

@Serializable
data class SavedSearch(
    val id: Int,
    val query: String,
    val labels: List<String>,
) {
    data class Result(
        val items: List<SavedSearch>,
        val isFromCache: Boolean,
    )
}
