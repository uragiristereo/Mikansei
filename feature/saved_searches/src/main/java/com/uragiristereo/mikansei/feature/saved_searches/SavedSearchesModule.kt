package com.uragiristereo.mikansei.feature.saved_searches

import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.uragiristereo.mikansei.core.ui.extension.rememberParentViewModelStoreOwner
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.SavedSearchesRoute
import com.uragiristereo.mikansei.feature.saved_searches.delete.DeleteSavedSearchContent
import com.uragiristereo.mikansei.feature.saved_searches.list.SavedSearchesListItemActions
import com.uragiristereo.mikansei.feature.saved_searches.new_or_edit.NewOrEditSavedSearchScreen
import com.uragiristereo.mikansei.feature.saved_searches.new_or_edit.NewOrEditSavedSearchViewModel
import com.uragiristereo.serializednavigationextension.navigation.compose.composable
import com.uragiristereo.serializednavigationextension.runtime.navigate
import com.uragiristereo.serializednavigationextension.runtime.navigation
import com.uragiristereo.serializednavigationextension.runtime.routeOf
import org.koin.androidx.compose.defaultExtras
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val savedSearchesModule = module {
    viewModelOf(::SavedSearchesViewModel)
    viewModelOf(::NewOrEditSavedSearchViewModel)
}

fun NavGraphBuilder.savedSearchesGraph(navController: NavHostController) {
    navigation(
        startDestination = SavedSearchesRoute.Index::class,
        route = MainRoute.SavedSearches::class,
    ) {
        composable<SavedSearchesRoute.Index> {
            val bottomSheetNavigator = LocalBottomSheetNavigator.current
            val parentViewModelStoreOwner = rememberParentViewModelStoreOwner(
                navController = navController,
                parentRoute = routeOf<MainRoute.SavedSearches>(),
            )

            SavedSearchesScreen(
                viewModel = koinViewModel(
                    viewModelStoreOwner = parentViewModelStoreOwner,
                    extras = defaultExtras(
                        viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
                            error("No LocalViewModelStoreOwner provided!")
                        },
                    ),
                ),
                onNavigateBack = navController::navigateUp,
                onNavigateNewSavedSearch = {
                    navController.navigate(
                        SavedSearchesRoute.NewOrEdit(
                            query = null,
                            savedSearch = null,
                        )
                    )
                },
                actions = SavedSearchesListItemActions(
                    onLabelClick = { label ->
                        navController.navigate(HomeRoute.Posts(tags = "search:$label ")) {
                            popUpTo(routeOf<HomeRoute.Posts>())
                        }
                    },
                    onQueryClick = { query ->
                        navController.navigate(HomeRoute.Posts(tags = "$query ")) {
                            popUpTo(routeOf<HomeRoute.Posts>())
                        }
                    },
                    onEditClick = { item ->
                        navController.navigate(
                            route = SavedSearchesRoute.NewOrEdit(
                                query = null,
                                savedSearch = item,
                            ),
                        )
                    },
                    onDeleteClick = { item ->
                        bottomSheetNavigator.navigate {
                            it.navigate(SavedSearchesRoute.Delete(savedSearch = item))
                        }
                    },
                )
            )
        }

        composable<SavedSearchesRoute.NewOrEdit> {
            val parentViewModelStoreOwner = rememberParentViewModelStoreOwner(
                navController = navController,
                parentRoute = routeOf<MainRoute.SavedSearches>(),
            )

            val savedSearchesViewModel: SavedSearchesViewModel = koinViewModel(
                viewModelStoreOwner = parentViewModelStoreOwner,
            )

            NewOrEditSavedSearchScreen(
                onNavigateBack = navController::navigateUp,
                onRefreshList = savedSearchesViewModel::refreshSavedSearches
            )
        }
    }
}

fun NavGraphBuilder.savedSearchesBottomRoute(navController: NavHostController) {
    composable<SavedSearchesRoute.Delete> {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val parentViewModelStoreOwner = rememberParentViewModelStoreOwner(
            navController = navController,
            parentRoute = routeOf<MainRoute.SavedSearches>(),
        )
        val savedSearchesViewModel: SavedSearchesViewModel = koinViewModel(
            viewModelStoreOwner = parentViewModelStoreOwner,
        )
        val args = rememberNavArgsOf()

        if (args != null) {
            DeleteSavedSearchContent(
                savedSearch = args.savedSearch,
                onDeleteClick = {
                    bottomSheetNavigator.runHiding {
                        savedSearchesViewModel.deleteSavedSearch(id = args.savedSearch.id)
                    }
                },
            )
        }
    }
}
