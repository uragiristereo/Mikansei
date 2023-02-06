package com.uragiristereo.mejiboard.core.booru.source.yandere.model.search

import kotlinx.serialization.Serializable

@Serializable
data class YandereSearch(
    val id: Int,
    val name: String,
    val count: Int,
    val type: Int,
)
