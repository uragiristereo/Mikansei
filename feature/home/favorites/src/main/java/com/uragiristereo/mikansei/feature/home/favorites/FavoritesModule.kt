package com.uragiristereo.mikansei.feature.home.favorites

import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.uragiristereo.mikansei.core.ui.extension.rememberParentNavBackStackEntry
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.InterceptBackGestureForBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.navigation.FavoriteNavType
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.PostNavType
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.addto.AddToFavGroupContent
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.addto.AddToFavGroupViewModel
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.delete.DeleteFavGroupContent
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.edit.EditFavGroupScreen
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.edit.EditFavGroupViewModel
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.more.FavGroupMoreContent
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.more.FavGroupMoreViewModel
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
    viewModelOf(::FavGroupMoreViewModel)
}

fun NavGraphBuilder.favoritesRoute(navController: NavHostController) {
    composable<HomeRoute.Favorites> {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val homeViewModelStoreOwner = navController.rememberParentNavBackStackEntry()

        CompositionLocalProvider(LocalViewModelStoreOwner provides homeViewModelStoreOwner) {
            FavoritesScreen(
                onFavoriteClick = { id, username ->
                    val tags = when (id) {
                        0 -> "ordfav:$username "
                        else -> "favgroup:$id status:any "
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

    composable<MainRoute.NewFavGroup> {
        val homeViewModelStoreOwner = navController.rememberParentNavBackStackEntry()
        val favoritesViewModel: FavoritesViewModel = koinViewModel(viewModelStoreOwner = homeViewModelStoreOwner)

        NewFavGroupScreen(
            onNavigateBack = navController::navigateUp,
            onSuccess = favoritesViewModel::getFavoritesAndFavoriteGroups,
        )
    }

    composable<HomeRoute.EditFavoriteGroup>(FavoriteNavType) {
        val homeViewModelStoreOwner = navController.rememberParentNavBackStackEntry()
        val favoritesViewModel: FavoritesViewModel = koinViewModel(viewModelStoreOwner = homeViewModelStoreOwner)

        EditFavGroupScreen(
            onNavigateBack = navController::navigateUp,
            onSuccess = favoritesViewModel::getFavoritesAndFavoriteGroups,
        )
    }
}

fun NavGraphBuilder.favoritesBottomRoute(navController: NavHostController) {
    composable<HomeRoute.AddToFavGroup>(PostNavType) {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        InterceptBackGestureForBottomSheetNavigator()

        AddToFavGroupContent(
            onDismiss = bottomSheetNavigator::hideSheet,
            onNewFavoriteGroupClick = { postId ->
                bottomSheetNavigator.runHiding {
                    navController.navigate(MainRoute.NewFavGroup(postId))
                }
            },
        )
    }

    composable<HomeRoute.FavoriteGroupMore>(FavoriteNavType) { entry ->
        val args = entry.toRoute<HomeRoute.FavoriteGroupMore>()

        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val favoriteGroup = args.favoriteGroup

        InterceptBackGestureForBottomSheetNavigator()

        FavGroupMoreContent(
            onDismiss = bottomSheetNavigator::hideSheet,
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

    composable<HomeRoute.DeleteFavoriteGroup>(FavoriteNavType) { entry ->
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val args = entry.toRoute<HomeRoute.DeleteFavoriteGroup>()
        val homeViewModelStoreOwner = navController.rememberParentNavBackStackEntry()
        val favoritesViewModel: FavoritesViewModel = koinViewModel(viewModelStoreOwner = homeViewModelStoreOwner)

        InterceptBackGestureForBottomSheetNavigator()

        DeleteFavGroupContent(
            favoriteGroup = args.favoriteGroup,
            onDeleteClick = {
                bottomSheetNavigator.runHiding {
                    favoritesViewModel.deleteFavoriteGroup(args.favoriteGroup)
                }
            },
        )
    }
}
