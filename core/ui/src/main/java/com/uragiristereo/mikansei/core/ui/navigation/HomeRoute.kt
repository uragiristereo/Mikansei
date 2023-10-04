package com.uragiristereo.mikansei.core.ui.navigation

import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.uragiristereo.mikansei.core.model.danbooru.Post
import kotlinx.serialization.Serializable

sealed interface HomeRoute : NavRoute {
    @Serializable
    data class Posts(
        val tags: String = "",
    ) : HomeRoute

    object Favorites : HomeRoute

    object More : HomeRoute

    @Serializable
    data class PostDialog(
        val post: Post,
    ) : HomeRoute

    @Serializable
    data class AddToFavGroup(
        val post: Post,
    ) : HomeRoute
}

val HomeRoutesString: List<String>
    get() = listOf(
        HomeRoute.Posts::class.route,
        HomeRoute.Favorites::class.route,
        HomeRoute.More::class.route,
    )

val HomeDialogRoutesString: List<String>
    get() = listOf(
        HomeRoute.PostDialog::class.route,
        HomeRoute.AddToFavGroup::class.route,
    )

val HomeAndDialogRoutesString: List<String>
    get() = HomeRoutesString + HomeDialogRoutesString
