package com.uragiristereo.mikansei.core.ui.navigation

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.serializednavigationextension.runtime.NavRoute
import com.uragiristereo.serializednavigationextension.runtime.routeOf
import kotlinx.serialization.Serializable

sealed interface HomeRoute : NavRoute {
    @Serializable
    data class Posts(
        val tags: String = "",
    ) : HomeRoute

    @Serializable
    data object Favorites : HomeRoute

    @Serializable
    data object More : HomeRoute

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
        routeOf<HomeRoute.Posts>(),
        routeOf<HomeRoute.Favorites>(),
        routeOf<HomeRoute.More>(),
    )
