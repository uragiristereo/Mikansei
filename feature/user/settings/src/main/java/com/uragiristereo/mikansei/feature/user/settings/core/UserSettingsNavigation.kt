package com.uragiristereo.mikansei.feature.user.settings.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.feature.user.settings.UserSettingsScreen
import com.uragiristereo.serializednavigationextension.navigation.compose.composable
import com.uragiristereo.serializednavigationextension.runtime.navigate

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
