package com.uragiristereo.mikansei.core.ui.navigation

import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
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
    data class PostMore(
        val post: Post,
    ) : HomeRoute

    @Serializable
    data class AddToFavGroup(
        val post: Post,
    ) : HomeRoute

    @Serializable
    data class Share(
        val post: Post,
        val showThumbnail: Boolean = true,
    ) : HomeRoute

    @Serializable
    data class FavoriteGroupMore(
        @Serializable
        val favoriteGroup: Favorite,
    ) : HomeRoute

    @Serializable
    data class EditFavoriteGroup(
        @Serializable
        val favoriteGroup: Favorite,
    ) : HomeRoute

    @Serializable
    data class DeleteFavoriteGroup(
        @Serializable
        val favoriteGroup: Favorite,
    ) : HomeRoute
}

val HomeRoutesString: List<String>
    get() = listOf(
        HomeRoute.Posts::class.route,
        HomeRoute.Favorites::class.route,
        HomeRoute.More::class.route,
    )
