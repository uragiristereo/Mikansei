package com.uragiristereo.mejiboard.core.ui.navigation

import com.uragiristereo.mejiboard.core.model.booru.post.Post
import com.uragiristereo.mejiboard.lib.navigation_extension.core.NavRoute
import kotlinx.serialization.Serializable

sealed interface MainRoute : NavRoute {
    @Serializable
    class Home : MainRoute {
        override val route: String = "home"
    }

    @Serializable
    data class Search(
        val tags: String = "",
    ) : MainRoute {
        override val route: String = "search"
    }

    @Serializable
    data class Image(
        val post: Post,
    ) : MainRoute {
        override val route: String = "image"
    }

    @Serializable
    class Settings : MainRoute {
        override val route: String = "settings"
    }

    @Serializable
    class Filters : MainRoute {
        override val route: String = "filters"
    }

    @Serializable
    class SearchHistory : MainRoute {
        override val route: String = "search_history"
    }

    @Serializable
    class SavedSearches : MainRoute {
        override val route: String = "saved_searches"
    }

    @Serializable
    class About : MainRoute {
        override val route: String = "about"
    }
}
