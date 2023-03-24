package com.uragiristereo.mikansei.feature.about.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.about.AboutScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.aboutRoute(
    navController: NavHostController,
) {
    composable(
        route = MainRoute.About,
        disableDeserialization = true,
        content = {
            AboutScreen(
                onNavigateBack = navController::navigateUp,
            )
        },
    )
}
