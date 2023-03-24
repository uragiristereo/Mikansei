package com.uragiristereo.mikansei.core.domain.entity.tag

data class Tag(
    val id: Int,
    val name: String,
    val category: TagCategory,
    val postCount: Long,
    val postCountFormatted: String,
    val isBlacklisted: Boolean = false,
)
