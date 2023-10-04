package com.uragiristereo.mikansei.core.domain.module.danbooru.entity

data class Tag(
    val id: Int? = null,
    val name: String,
    val category: Category,
    val postCount: Long,
    val postCountFormatted: String,
    val isBlacklisted: Boolean? = null,
    val antecedent: String? = null,
) {
    enum class Category {
        GENERAL,
        ARTIST,
        UNKNOWN,
        COPYRIGHT,
        CHARACTER,
        META,
    }
}
