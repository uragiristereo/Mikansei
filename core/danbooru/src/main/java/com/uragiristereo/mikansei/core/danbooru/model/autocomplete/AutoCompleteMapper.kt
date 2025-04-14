package com.uragiristereo.mikansei.core.danbooru.model.autocomplete

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.AutoComplete
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag

fun DanbooruAutoComplete.toAutoComplete(searchType: AutoComplete.SearchType): AutoComplete {
    return when (searchType) {
        AutoComplete.SearchType.TAG_QUERY -> toAutoCompleteTag()
        AutoComplete.SearchType.WIKI_PAGE -> toAutoCompleteTag()
    }
}

private fun DanbooruAutoComplete.toAutoCompleteTag(): AutoComplete.Tag {
    return AutoComplete.Tag(
        value = value,
        category = when (category) {
            0 -> Tag.Category.GENERAL
            1 -> Tag.Category.ARTIST
            2 -> Tag.Category.UNKNOWN
            3 -> Tag.Category.COPYRIGHT
            4 -> Tag.Category.CHARACTER
            5 -> Tag.Category.META
            else -> Tag.Category.UNKNOWN
        },
        postCount = postCount,
        antecedent = antecedent,
    )
}

fun List<DanbooruAutoComplete>.toAutoCompleteList(
    searchType: AutoComplete.SearchType,
): List<AutoComplete> {
    return map { it.toAutoComplete(searchType) }
}
