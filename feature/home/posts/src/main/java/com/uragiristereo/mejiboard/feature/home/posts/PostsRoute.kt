package com.uragiristereo.mejiboard.feature.home.posts

import com.uragiristereo.mejiboard.lib.navigation_extension.NavRoute
import kotlinx.serialization.Serializable

sealed interface PostsRoute : NavRoute {
    @Serializable
    data class IndexRoute(
        val tags: String,
    ) : PostsRoute {
        override val route: String = "/home/posts/index"
    }
}
