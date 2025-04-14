package com.uragiristereo.mikansei.core.ui.navigation

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.AutoComplete
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.serializednavigationextension.runtime.NavRoute
import com.uragiristereo.serializednavigationextension.runtime.routeOf
import kotlinx.serialization.Serializable

sealed interface MainRoute : NavRoute {
    @Serializable
    data object Home : MainRoute

    @Serializable
    data class Search(
        val tags: String = "",
        val searchType: AutoComplete.SearchType = AutoComplete.SearchType.TAG_QUERY,
    ) : MainRoute

    @Serializable
    data class Image(
        val post: Post,
    ) : MainRoute

    @Serializable
    data object Settings : MainRoute

    @Serializable
    data object Filters : MainRoute

    @Serializable
    data object SearchHistory : MainRoute

    @Serializable
    data object About : MainRoute

    @Serializable
    data object User : MainRoute

    @Serializable
    data class NewFavGroup(
        val postId: Int? = null,
    ) : MainRoute

    @Serializable
    data object SavedSearches : MainRoute

    @Serializable
    data object Wiki : MainRoute
}

val NestedNavigationRoutes = listOf(
    routeOf<MainRoute.Home>(),
    routeOf<MainRoute.Settings>(),
    routeOf<MainRoute.User>(),
    routeOf<MainRoute.Wiki>(),
)
