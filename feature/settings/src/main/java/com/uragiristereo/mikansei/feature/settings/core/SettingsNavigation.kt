package com.uragiristereo.mikansei.feature.settings.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.animation.navigation
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.SettingsRoute
import com.uragiristereo.mikansei.feature.settings.SettingsScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
) {
    navigation(
        startDestination = SettingsRoute.Index::class,
        route = MainRoute.Settings::class,
    ) {
        composable(
            route = SettingsRoute.Index,
            content = {
                SettingsScreen(
                    onNavigateBack = navController::navigateUp,
                )
            },
        )
    }
}
