package com.uragiristereo.mejiboard.presentation.common.entity

import java.util.UUID

data class FilterItem(
    val id: String = UUID.randomUUID().toString(),
    val tag: String,
    val enabled: Boolean = true,
    val selected: Boolean = false,
)
