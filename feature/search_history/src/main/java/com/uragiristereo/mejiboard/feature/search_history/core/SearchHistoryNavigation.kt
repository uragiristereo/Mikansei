package com.uragiristereo.mejiboard.feature.search_history.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.uragiristereo.mejiboard.core.ui.navigation.MainRoute
import com.uragiristereo.mejiboard.feature.search_history.SearchHistoryScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.searchHistoryRoute(
    navController: NavHostController,
) {
    composable(
        route = MainRoute.SearchHistory,
        disableDeserialization = true,
        content = {
            SearchHistoryScreen(
                onNavigateBack = navController::navigateUp,
            )
        },
    )
}
