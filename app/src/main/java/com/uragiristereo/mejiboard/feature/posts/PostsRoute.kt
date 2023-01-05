package com.uragiristereo.mejiboard.feature.posts

import com.uragiristereo.mejiboard.core.common.ui.navigation.NavigationRoute

sealed class PostsRoute(
    route: String,
    argsKeys: List<String> = listOf(),
) : NavigationRoute(route, argsKeys) {
    object Index : PostsRoute(
        route = "home/index",
        argsKeys = listOf("tags"),
    )
}
