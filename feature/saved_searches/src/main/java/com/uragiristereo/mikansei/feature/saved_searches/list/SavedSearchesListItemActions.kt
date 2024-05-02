package com.uragiristereo.mikansei.feature.saved_searches.list

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.SavedSearch

data class SavedSearchesListItemActions(
    val onLabelClick: (label: String) -> Unit = {},
    val onQueryClick: (query: String) -> Unit = {},
    val onEditClick: (item: SavedSearch) -> Unit = {},
    val onDeleteClick: (item: SavedSearch) -> Unit = {},
)
