package com.uragiristereo.mikansei.feature.user.settings.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.compose.composable
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.feature.user.settings.UserSettingsScreen

fun NavGraphBuilder.userSettingsRoute(
    navController: NavHostController,
) {
    composable(
        route = UserRoute.Settings,
        content = {
            UserSettingsScreen(
                onNavigateBack = navController::navigateUp,
            )
        },
    )
}
