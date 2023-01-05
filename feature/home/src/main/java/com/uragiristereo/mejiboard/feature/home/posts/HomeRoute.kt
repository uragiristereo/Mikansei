package com.uragiristereo.mejiboard.feature.home.posts

import com.uragiristereo.mejiboard.core.model.navigation.NavigationRoute

sealed class HomeRoute(
    route: String,
    argsKeys: List<String> = listOf(),
) : NavigationRoute(route, argsKeys) {
    object Posts : HomeRoute(route = "home/posts")

    object Collections : HomeRoute(route = "home/collections")

    object More : HomeRoute(route = "home/more")
}
