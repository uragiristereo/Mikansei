package com.uragiristereo.mikansei.feature.home.favorites.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.dialog
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.home.favorites.FavoritesScreen
import com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.AddToFavGroupDialog
import com.uragiristereo.mikansei.feature.home.favorites.new_fav_group.NewFavGroupScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.favoritesRoute(navController: NavHostController) {
    composable(
        route = HomeRoute.Favorites,
        content = {
            FavoritesScreen(
                onFavoritesClick = { id, userName ->
                    val tags = when (id) {
                        0 -> "ordfav:$userName "
                        else -> "favgroup:$id "
                    }

                    navController.navigate(HomeRoute.Posts(tags)) {
                        popUpTo(id = navController.graph.findStartDestination().id)
                    }
                },
                onAddClick = {
                    navController.navigate(MainRoute.NewFavGroup())
                }
            )
        },
    )

    dialog(
        route = HomeRoute.AddToFavGroup::class,
        content = {
            AddToFavGroupDialog(
                onDismiss = navController::popBackStack,
                onNewFavoriteGroupClick = { postId ->
                    navController.navigate(MainRoute.NewFavGroup(postId))
                },
            )
        },
    )

    composable(
        route = MainRoute.NewFavGroup(),
        content = {
            NewFavGroupScreen(
                onNavigateBack = navController::navigateUp,
            )
        }
    )
}
