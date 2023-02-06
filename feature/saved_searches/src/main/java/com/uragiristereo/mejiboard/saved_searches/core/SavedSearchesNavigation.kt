package com.uragiristereo.mejiboard.saved_searches.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.uragiristereo.mejiboard.core.ui.navigation.MainRoute
import com.uragiristereo.mejiboard.saved_searches.SavedSearchesScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.savedSearchesRoute(
    navController: NavHostController,
) {
    composable(
        route = MainRoute.SavedSearches,
        disableDeserialization = true,
        content = {
            SavedSearchesScreen(
                onNavigateBack = navController::navigateUp,
            )
        },
    )
}
