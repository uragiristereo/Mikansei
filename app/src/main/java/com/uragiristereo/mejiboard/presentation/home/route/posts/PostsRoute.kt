package com.uragiristereo.mejiboard.presentation.home.route.posts

import com.uragiristereo.mejiboard.presentation.common.navigation.NavigationRoute

sealed class PostsRoute(
    route: String,
    argsKeys: List<String> = listOf(),
) : NavigationRoute(route, argsKeys) {
    object Index : PostsRoute(
        route = "home/index",
        argsKeys = listOf("tags"),
    )
}