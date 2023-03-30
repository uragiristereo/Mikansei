package com.uragiristereo.mikansei.core.ui.navigation

import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import com.github.uragiristereo.safer.compose.navigation.core.route
import kotlinx.serialization.Serializable

sealed interface HomeRoute : NavRoute {
    @Serializable
    data class Posts(
        val tags: String = "",
    ) : HomeRoute

    object Favorites : HomeRoute

    object More : HomeRoute
}

val HomeRoutes: List<HomeRoute>
    get() = listOf(
        HomeRoute.Posts(),
        HomeRoute.Favorites,
        HomeRoute.More,
    )

val HomeRoutesString: List<String>
    get() = HomeRoutes.map { it::class.route }
