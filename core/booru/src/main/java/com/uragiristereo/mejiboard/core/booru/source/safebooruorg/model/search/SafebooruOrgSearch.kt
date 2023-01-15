package com.uragiristereo.mejiboard.core.booru.source.safebooruorg.model.search

import kotlinx.serialization.Serializable

@Serializable
data class SafebooruOrgSearch(
    val label: String,
    val value: String,
)
