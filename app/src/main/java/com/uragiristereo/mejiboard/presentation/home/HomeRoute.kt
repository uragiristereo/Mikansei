package com.uragiristereo.mejiboard.presentation.home

import com.uragiristereo.mejiboard.presentation.common.navigation.NavigationRoute

sealed class HomeRoute(
    route: String,
    argsKeys: List<String> = listOf(),
) : NavigationRoute(route, argsKeys) {
    object Posts : HomeRoute(route = "home/posts")

    object Collections : HomeRoute(route = "home/collections")

    object More : HomeRoute(route = "home/more")
}
