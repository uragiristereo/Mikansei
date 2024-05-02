package com.uragiristereo.mikansei.core.danbooru.model.saved_search

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.SavedSearch

fun DanbooruSavedSearch.toSavedSearch(): SavedSearch {
    return SavedSearch(
        id = id,
        query = query,
        labels = labels,
    )
}

fun List<DanbooruSavedSearch>.toSavedSearchList(): List<SavedSearch> {
    return map {
        it.toSavedSearch()
    }
}
