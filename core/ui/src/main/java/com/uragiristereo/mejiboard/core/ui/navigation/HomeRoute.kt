package com.uragiristereo.mejiboard.core.ui.navigation

import com.uragiristereo.mejiboard.lib.navigation_extension.NavRoute
import kotlinx.serialization.Serializable

sealed interface HomeRoute : NavRoute {
    @Serializable
    class Posts : HomeRoute {
        override val route: String = "home/posts"
    }

    @Serializable
    class Collections : HomeRoute {
        override val route: String = "home/collections"
    }

    @Serializable
    class More : HomeRoute {
        override val route: String = "home/more"
    }
}
