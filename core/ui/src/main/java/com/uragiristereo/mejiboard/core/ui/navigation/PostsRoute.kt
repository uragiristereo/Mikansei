package com.uragiristereo.mejiboard.core.ui.navigation

import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import com.github.uragiristereo.safer.compose.navigation.core.routeWithData
import kotlinx.serialization.Serializable

sealed interface PostsRoute : NavRoute {
    @Serializable
    data class Index(
        val tags: String = "",
    ) : PostsRoute {
        override val route: String = "home/posts/index".routeWithData()
    }
}
