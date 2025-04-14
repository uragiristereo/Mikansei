package com.uragiristereo.mikansei.core.domain.module.danbooru.entity

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag.Category as TagCategory

sealed interface AutoComplete {
    val value: String

    data class Tag(
        override val value: String,
        val category: TagCategory,
        val postCount: Long?,
        val antecedent: String?,
    ) : AutoComplete

    enum class SearchType {
        TAG_QUERY,
        WIKI_PAGE,
    }
}
