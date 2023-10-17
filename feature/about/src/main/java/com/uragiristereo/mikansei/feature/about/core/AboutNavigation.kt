package com.uragiristereo.mikansei.feature.about.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.compose.composable
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.about.AboutScreen

fun NavGraphBuilder.aboutRoute(
    navController: NavHostController,
) {
    composable(
        route = MainRoute.About,
        content = {
            AboutScreen(
                onNavigateBack = navController::navigateUp,
            )
        },
    )
}
