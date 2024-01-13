package com.uragiristereo.mikansei.feature.settings.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.SettingsRoute
import com.uragiristereo.mikansei.feature.settings.SettingsScreen
import com.uragiristereo.serializednavigationextension.navigation.compose.composable
import com.uragiristereo.serializednavigationextension.runtime.navigation

fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
) {
    navigation(
        startDestination = SettingsRoute.Index::class,
        route = MainRoute.Settings::class,
    ) {
        composable<SettingsRoute.Index> {
            SettingsScreen(
                onNavigateBack = navController::navigateUp,
            )
        }
    }
}
