package com.uragiristereo.mejiboard.feature.home.posts

import com.uragiristereo.mejiboard.core.model.navigation.NavigationRoute

sealed class PostsRoute(
    route: String,
    argsKeys: List<String> = listOf(),
) : NavigationRoute(route, argsKeys) {
    object Index : PostsRoute(
        route = "home/index",
        argsKeys = listOf("tags"),
    )
}
