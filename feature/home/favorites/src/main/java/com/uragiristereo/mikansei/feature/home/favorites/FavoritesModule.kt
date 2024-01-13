package com.uragiristereo.mikansei.feature.home.favorites

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
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
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.more.FavGroupMoreViewModel
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.NewFavGroupScreen
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.NewFavGroupViewModel
import com.uragiristereo.serializednavigationextension.navigation.compose.composable
import com.uragiristereo.serializednavigationextension.runtime.navigate
import com.uragiristereo.serializednavigationextension.runtime.routeOf
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

        val homeViewModelStoreOwner = rememberParentViewModelStoreOwner(
            navController = navController,
            parentRoute = routeOf<MainRoute.Home>(),
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

    composable(defaultValue = MainRoute.NewFavGroup()) {
        val homeViewModelStoreOwner = rememberParentViewModelStoreOwner(
            navController = navController,
            parentRoute = routeOf<MainRoute.Home>(),
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
            parentRoute = routeOf<MainRoute.Home>(),
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

    composable<HomeRoute.FavoriteGroupMore> {
        val args = rememberNavArgsOf()

        if (args != null) {
            val bottomSheetNavigator = LocalBottomSheetNavigator.current
            val favoriteGroup = args.favoriteGroup

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

    composable<HomeRoute.DeleteFavoriteGroup> {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val args = rememberNavArgsOf()

        val homeViewModelStoreOwner = rememberParentViewModelStoreOwner(
            navController = navController,
            parentRoute = routeOf<MainRoute.Home>(),
        )

        val favoritesViewModel: FavoritesViewModel = koinViewModel(viewModelStoreOwner = homeViewModelStoreOwner)

        if (args != null) {
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
}
