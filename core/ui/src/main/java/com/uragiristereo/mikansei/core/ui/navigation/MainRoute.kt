package com.uragiristereo.mikansei.core.ui.navigation

import com.uragiristereo.mikansei.core.model.danbooru.Post
import kotlinx.serialization.Serializable

sealed interface MainRoute : NavRoute {
    @Serializable
    data object Home : MainRoute

    @Serializable
    data class Search(
        val tags: String = "",
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
}

val NestedNavigationRoutes = listOf(
    routeOf<MainRoute.Home>(),
    routeOf<MainRoute.Settings>(),
    routeOf<MainRoute.User>(),
)
