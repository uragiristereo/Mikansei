package com.uragiristereo.mikansei.core.domain.module.danbooru.entity

import java.time.OffsetDateTime

data class Wiki(
    val id: Int,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val title: String,
    val body: String,
    val isLocked: Boolean,
    val isDeleted: Boolean,
    val otherNames: List<String>,
)
