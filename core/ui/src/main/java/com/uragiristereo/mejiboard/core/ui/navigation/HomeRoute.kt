package com.uragiristereo.mejiboard.core.ui.navigation

import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import kotlinx.serialization.Serializable

sealed interface HomeRoute : NavRoute {
    @Serializable
    object Posts : HomeRoute {
        override val route: String = "home/posts"
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
