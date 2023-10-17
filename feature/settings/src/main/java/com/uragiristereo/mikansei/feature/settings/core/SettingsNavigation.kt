package com.uragiristereo.mikansei.feature.settings.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.compose.composable
import com.github.uragiristereo.safer.compose.navigation.compose.navigation
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.SettingsRoute
import com.uragiristereo.mikansei.feature.settings.SettingsScreen

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
