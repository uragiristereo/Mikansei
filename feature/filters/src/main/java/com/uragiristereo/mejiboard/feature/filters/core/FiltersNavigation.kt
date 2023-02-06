package com.uragiristereo.mejiboard.feature.filters.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.uragiristereo.mejiboard.core.ui.navigation.MainRoute
import com.uragiristereo.mejiboard.feature.filters.FiltersScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.filtersRoute(
    navController: NavHostController,
) {
    composable(
        route = MainRoute.Filters,
        disableDeserialization = true,
        content = {
            FiltersScreen(
                onNavigateBack = navController::navigateUp,
            )
        },
    )
}
