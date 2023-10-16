package com.uragiristereo.mikansei.feature.home.favorites.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.uragiristereo.mikansei.core.ui.extension.rememberParentViewModelStoreOwner
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.home.favorites.FavoritesScreen
import com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.AddToFavGroupContent
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

    composable(
        route = MainRoute.NewFavGroup(),
        content = {
            NewFavGroupScreen(
                onNavigateBack = navController::navigateUp,
            )
        }
    )
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
fun NavGraphBuilder.favoritesBottomRoute(mainNavController: NavHostController) {
    composable<HomeRoute.AddToFavGroup> {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        AddToFavGroupContent(
            onDismiss = bottomSheetNavigator.bottomSheetState::hide,
            onNewFavoriteGroupClick = { postId ->
                bottomSheetNavigator.runHiding {
                    mainNavController.navigate(MainRoute.NewFavGroup(postId))
                }
            },
        )
    }
}
