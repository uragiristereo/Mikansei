package com.uragiristereo.mikansei.feature.home.favorites.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.feature.home.favorites.FavoritesScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.favoritesRoute(navController: NavHostController) {
    composable(
        route = HomeRoute.Favorites,
        content = {
            FavoritesScreen(
                onFavoritesClick = { id, userName ->
                    val tags = when (id) {
                        0 -> "ordfav:$userName"
                        else -> "favgroup:$id"
                    }

                    navController.navigate(HomeRoute.Posts(tags)) {
                        popUpTo(id = navController.graph.findStartDestination().id)
                    }
                },
            )
        },
    )
}
