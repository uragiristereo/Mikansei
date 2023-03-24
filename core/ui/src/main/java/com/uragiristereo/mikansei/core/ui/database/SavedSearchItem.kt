package com.uragiristereo.mikansei.core.ui.database

import java.util.*

data class SavedSearchItem(
    val id: String = UUID.randomUUID().toString(),
    val tag: String,
)
