package com.uragiristereo.mejiboard.core.ui.navigation

import com.uragiristereo.mejiboard.lib.navigation_extension.NavRoute
import kotlinx.serialization.Serializable

sealed interface PostsRoute : NavRoute {
    @Serializable
    data class Index(
        val tags: String,
    ) : PostsRoute {
        override val route: String = "home/posts/index"
    }
}