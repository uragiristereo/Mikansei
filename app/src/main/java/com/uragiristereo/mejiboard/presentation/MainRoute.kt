package com.uragiristereo.mejiboard.presentation

import com.uragiristereo.mejiboard.presentation.common.navigation.NavigationRoute

sealed class MainRoute(
    route: String,
    argsKeys: List<String> = listOf(),
) : NavigationRoute(route, argsKeys) {
    object Home : MainRoute(route = "home")

    object Search : MainRoute(
        route = "search",
        argsKeys = listOf("tags"),
    )

    object Image : MainRoute(
        route = "image",
        argsKeys = listOf("post"),
    )

    object Settings : MainRoute(route = "settings")

    object Filters : MainRoute(route = "filters")

    object SearchHistory : MainRoute(route = "search_history")
    object SavedSearches : MainRoute(route = "saved_searches")

    object About : MainRoute(route = "about")
}