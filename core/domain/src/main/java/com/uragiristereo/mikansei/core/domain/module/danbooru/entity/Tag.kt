package com.uragiristereo.mikansei.core.domain.module.danbooru.entity

import androidx.compose.ui.graphics.Color

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

fun Tag.Category.getCategoryColor(isLight: Boolean): Color {
    return when {
        isLight -> when (this) {
            Tag.Category.GENERAL -> Color(0xFF0075F8)
            Tag.Category.CHARACTER -> Color(0xFF00AB2C)
            Tag.Category.COPYRIGHT -> Color(0xFFA800AA)
            Tag.Category.ARTIST -> Color(0xFFC00004)
            Tag.Category.META -> Color(0xFFFD9200)
            Tag.Category.UNKNOWN -> Color(0xFF0075F8)
        }

        else -> when (this) {
            Tag.Category.GENERAL -> Color(0xFF009BE6)
            Tag.Category.CHARACTER -> Color(0xFF35C64A)
            Tag.Category.COPYRIGHT -> Color(0xFFC797FF)
            Tag.Category.ARTIST -> Color(0xFFFF8A8B)
            Tag.Category.META -> Color(0xFFEAD084)
            Tag.Category.UNKNOWN -> Color(0xFF009BE6)
        }
    }
}
