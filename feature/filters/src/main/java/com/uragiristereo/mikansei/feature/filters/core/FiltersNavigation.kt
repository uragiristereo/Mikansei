package com.uragiristereo.mikansei.feature.filters.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.filters.FiltersScreen
import com.uragiristereo.serializednavigationextension.navigation.compose.composable

fun NavGraphBuilder.filtersRoute(
    navController: NavHostController,
) {
    composable<MainRoute.Filters> {
        FiltersScreen(
            onNavigateBack = navController::navigateUp,
        )
    }
}
