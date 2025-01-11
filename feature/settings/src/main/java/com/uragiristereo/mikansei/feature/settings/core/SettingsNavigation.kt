package com.uragiristereo.mikansei.feature.settings.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.SettingsRoute
import com.uragiristereo.mikansei.feature.settings.SettingsScreen

fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
) {
    navigation<MainRoute.Settings>(startDestination = SettingsRoute.Index) {
        composable<SettingsRoute.Index> {
            SettingsScreen(
                onNavigateBack = navController::navigateUp,
            )
        }
    }
}
