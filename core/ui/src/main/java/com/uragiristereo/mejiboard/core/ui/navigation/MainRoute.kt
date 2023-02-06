package com.uragiristereo.mejiboard.core.ui.navigation

import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import kotlinx.serialization.Serializable

sealed interface MainRoute : NavRoute {
    object Home : MainRoute

    @Serializable
    data class Search(
        val tags: String = "",
    ) : MainRoute

    @Serializable
    data class Image(
        val post: Post,
    ) : MainRoute

    object Settings : MainRoute

    object Filters : MainRoute

    object SearchHistory : MainRoute

    object SavedSearches : MainRoute

    object About : MainRoute
}
