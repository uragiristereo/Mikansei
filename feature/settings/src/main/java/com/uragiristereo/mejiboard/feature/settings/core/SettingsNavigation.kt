package com.uragiristereo.mejiboard.feature.settings.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.animation.navigation
import com.uragiristereo.mejiboard.core.ui.navigation.MainRoute
import com.uragiristereo.mejiboard.core.ui.navigation.SettingsRoute
import com.uragiristereo.mejiboard.feature.settings.SettingsScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
) {
    navigation(
        startDestination = SettingsRoute.Index,
        route = MainRoute.Settings,
    ) {
        composable(
            route = SettingsRoute.Index,
            disableDeserialization = true,
            content = {
                SettingsScreen(
                    onNavigateBack = navController::navigateUp,
                )
            },
        )
    }
}
