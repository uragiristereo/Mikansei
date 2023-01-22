package com.uragiristereo.mejiboard.core.common.ui.entity

import java.util.UUID

data class SavedSearchItem(
    val id: String = UUID.randomUUID().toString(),
    val tag: String,
)
