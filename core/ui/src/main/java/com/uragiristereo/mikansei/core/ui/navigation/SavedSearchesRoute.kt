package com.uragiristereo.mikansei.core.ui.navigation

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.SavedSearch
import com.uragiristereo.serializednavigationextension.runtime.NavRoute
import kotlinx.serialization.Serializable

sealed interface SavedSearchesRoute : NavRoute {
    @Serializable
    data object Index : SavedSearchesRoute

    @Serializable
    data class NewOrEdit(
        val query: String?,
        val savedSearch: SavedSearch?,
    ) : SavedSearchesRoute

    @Serializable
    data class Delete(val savedSearch: SavedSearch) : SavedSearchesRoute
}
