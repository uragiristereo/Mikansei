package com.uragiristereo.mikansei.feature.home.favorites

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.compose.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.uragiristereo.mikansei.core.ui.extension.rememberParentViewModelStoreOwner
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.addto.AddToFavGroupContent
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.addto.AddToFavGroupViewModel
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.NewFavGroupScreen
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.NewFavGroupViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val favoritesModule = module {
    viewModelOf(::FavoritesViewModel)
    viewModelOf(::AddToFavGroupViewModel)
    viewModelOf(::NewFavGroupViewModel)
}

fun NavGraphBuilder.favoritesRoute(navController: NavHostController) {
    composable<HomeRoute.Favorites> {
        val homeViewModelStoreOwner = rememberParentViewModelStoreOwner(
            navController = navController,
            parentRoute = MainRoute.Home.route,
        )

        CompositionLocalProvider(LocalViewModelStoreOwner provides homeViewModelStoreOwner) {
            FavoritesScreen(
                onFavoritesClick = { id, username ->
                    val tags = when (id) {
                        0 -> "ordfav:$username "
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
    }

    composable(route = MainRoute.NewFavGroup()) {
        NewFavGroupScreen(onNavigateBack = navController::navigateUp)
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.favoritesBottomRoute(navController: NavHostController) {
    composable<HomeRoute.AddToFavGroup> {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        AddToFavGroupContent(
            onDismiss = bottomSheetNavigator.bottomSheetState::hide,
            onNewFavoriteGroupClick = { postId ->
                bottomSheetNavigator.runHiding {
                    navController.navigate(MainRoute.NewFavGroup(postId))
                }
            },
        )
    }
}
