package com.uragiristereo.mejiboard.core.ui.navigation

import com.uragiristereo.mejiboard.core.model.booru.post.Post
import com.uragiristereo.mejiboard.lib.navigation_extension.core.NavRoute
import com.uragiristereo.mejiboard.lib.navigation_extension.core.routeWithData
import kotlinx.serialization.Serializable

sealed interface MainRoute : NavRoute {
    @Serializable
    object Home : MainRoute {
        override val route: String = "home"
    }

    @Serializable
    data class Search(
        val tags: String = "",
    ) : MainRoute {
        override val route: String = "search".routeWithData()
    }

    @Serializable
    data class Image(
        val post: Post,
    ) : MainRoute {
        override val route: String = "image".routeWithData()
    }

    @Serializable
    object Settings : MainRoute {
        override val route: String = "settings"
    }

    @Serializable
    object Filters : MainRoute {
        override val route: String = "filters"
    }

    @Serializable
    object SearchHistory : MainRoute {
        override val route: String = "search_history"
    }

    @Serializable
    object SavedSearches : MainRoute {
        override val route: String = "saved_searches"
    }

    @Serializable
    object About : MainRoute {
        override val route: String = "about"
    }
}
