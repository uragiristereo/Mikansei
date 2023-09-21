package com.uragiristereo.mikansei.feature.home.favorites.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.dialog
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.uragiristereo.mikansei.core.ui.extension.rememberParentViewModelStoreOwner
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
            val homeViewModelStoreOwner = rememberParentViewModelStoreOwner(
                navController = navController,
                parentRoute = MainRoute.Home.route,
            )

            CompositionLocalProvider(LocalViewModelStoreOwner provides homeViewModelStoreOwner) {
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
                    },
                )
            }
        },
    )

    dialog<HomeRoute.AddToFavGroup> {
        AddToFavGroupDialog(
            onDismiss = navController::popBackStack,
            onNewFavoriteGroupClick = { postId ->
                navController.navigate(MainRoute.NewFavGroup(postId))
            },
        )
    }

    composable(
        route = MainRoute.NewFavGroup(),
        content = {
            NewFavGroupScreen(
                onNavigateBack = navController::navigateUp,
            )
        }
    )
}
