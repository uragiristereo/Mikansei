package com.uragiristereo.mejiboard.core.ui.navigation

import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import com.github.uragiristereo.safer.compose.navigation.core.routeWithData
import kotlinx.serialization.Serializable

sealed interface HomeRoute : NavRoute {
    @Serializable
    data class Posts(
        val tags: String = "",
    ) : HomeRoute {
        override val route: String = "home/posts".routeWithData()
    }

    @Serializable
    object Collections : HomeRoute {
        override val route: String = "home/collections"
    }

    @Serializable
    object More : HomeRoute {
        override val route: String = "home/more"
    }
}

val HomeRoutes: List<HomeRoute>
    get() = listOf(
        HomeRoute.Posts(),
        HomeRoute.Collections,
        HomeRoute.More,
    )

val HomeRoutesString: List<String>
    get() = HomeRoutes.map { it.route }
