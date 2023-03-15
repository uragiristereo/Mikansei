package com.uragiristereo.mejiboard.domain.entity.tag

data class Tag(
    val id: Int,
    val name: String,
    val category: TagCategory,
    val postCount: Long,
    val postCountFormatted: String,
    val isBlacklisted: Boolean = false,
)
