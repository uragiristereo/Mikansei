package com.uragiristereo.mikansei.core.domain.module.database.entity

data class Session(
    val id: String,
    val tags: String,
    val scrollIndex: Int = 0,
    val scrollOffset: Int = 0,
)
