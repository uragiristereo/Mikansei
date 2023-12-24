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
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.delete.DeleteFavGroupContent
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.edit.EditFavGroupScreen
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.edit.EditFavGroupViewModel
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.more.FavGroupMoreContent
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.NewFavGroupScreen
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.NewFavGroupViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val favoritesModule = module {
    viewModelOf(::FavoritesViewModel)
    viewModelOf(::AddToFavGroupViewModel)
    viewModelOf(::NewFavGroupViewModel)
    viewModelOf(::EditFavGroupViewModel)
}

fun NavGraphBuilder.favoritesRoute(navController: NavHostController) {
    composable<HomeRoute.Favorites> {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        val homeViewModelStoreOwner = rememberParentViewModelStoreOwner(
            navController = navController,
            parentRoute = MainRoute.Home.route,
        )

        CompositionLocalProvider(LocalViewModelStoreOwner provides homeViewModelStoreOwner) {
            FavoritesScreen(
                onFavoriteClick = { id, username ->
                    val tags = when (id) {
                        0 -> "ordfav:$username "
                        else -> "favgroup:$id "
                    }

                    navController.navigate(HomeRoute.Posts(tags)) {
                        popUpTo(id = navController.graph.findStartDestination().id)
                    }
                },
                onFavGroupLongClick = { favoriteGroup ->
                    bottomSheetNavigator.navigate {
                        it.navigate(HomeRoute.FavoriteGroupMore(favoriteGroup))
                    }
                },
                onAddClick = {
                    navController.navigate(MainRoute.NewFavGroup())
                },
            )
        }
    }

    composable(route = MainRoute.NewFavGroup()) {
        val homeViewModelStoreOwner = rememberParentViewModelStoreOwner(
            navController = navController,
            parentRoute = MainRoute.Home.route,
        )

        val favoritesViewModel: FavoritesViewModel = koinViewModel(viewModelStoreOwner = homeViewModelStoreOwner)

        NewFavGroupScreen(
            onNavigateBack = navController::navigateUp,
            onSuccess = favoritesViewModel::getFavoritesAndFavoriteGroups,
        )
    }

    composable<HomeRoute.EditFavoriteGroup> {
        val homeViewModelStoreOwner = rememberParentViewModelStoreOwner(
            navController = navController,
            parentRoute = MainRoute.Home.route,
        )

        val favoritesViewModel: FavoritesViewModel = koinViewModel(viewModelStoreOwner = homeViewModelStoreOwner)

        EditFavGroupScreen(
            onNavigateBack = navController::navigateUp,
            onSuccess = favoritesViewModel::getFavoritesAndFavoriteGroups,
        )
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

    composable<HomeRoute.FavoriteGroupMore> { data ->
        if (data != null) {
            val bottomSheetNavigator = LocalBottomSheetNavigator.current
            val favoriteGroup = data.favoriteGroup

            FavGroupMoreContent(
                onDismiss = bottomSheetNavigator.bottomSheetState::hide,
                favoriteGroup = favoriteGroup,
                onFavGroupClick = {
                    bottomSheetNavigator.runHiding {
                        val tags = "favgroup:${favoriteGroup.id} "

                        navController.navigate(HomeRoute.Posts(tags)) {
                            popUpTo(id = navController.graph.findStartDestination().id)
                        }
                    }
                },
                onEditFavGroupClick = {
                    bottomSheetNavigator.runHiding {
                        navController.navigate(HomeRoute.EditFavoriteGroup(favoriteGroup))
                    }
                },
                onDeleteFavGroupClick = {
                    bottomSheetNavigator.navigate {
                        it.navigate(HomeRoute.DeleteFavoriteGroup(favoriteGroup))
                    }
                },
            )
        }
    }

    composable<HomeRoute.DeleteFavoriteGroup> { data ->
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        val homeViewModelStoreOwner = rememberParentViewModelStoreOwner(
            navController = navController,
            parentRoute = MainRoute.Home.route,
        )

        val favoritesViewModel: FavoritesViewModel = koinViewModel(viewModelStoreOwner = homeViewModelStoreOwner)

        if (data != null) {
            DeleteFavGroupContent(
                favoriteGroup = data.favoriteGroup,
                onDeleteClick = {
                    bottomSheetNavigator.runHiding {
                        favoritesViewModel.deleteFavoriteGroup(data.favoriteGroup)
                    }
                },
            )
        }
    }
}
