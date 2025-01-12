package com.uragiristereo.mikansei.feature.user.settings.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.feature.user.settings.UserSettingsScreen

fun NavGraphBuilder.userSettingsRoute(
    navController: NavHostController,
) {
    composable<UserRoute.Settings> {
        UserSettingsScreen(
            onNavigate = navController::navigate,
            onNavigateBack = navController::navigateUp,
        )
    }
}
