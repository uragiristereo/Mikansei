package com.uragiristereo.mejiboard.core.ui.database

import java.util.UUID

data class SavedSearchItem(
    val id: String = UUID.randomUUID().toString(),
    val tag: String,
)
