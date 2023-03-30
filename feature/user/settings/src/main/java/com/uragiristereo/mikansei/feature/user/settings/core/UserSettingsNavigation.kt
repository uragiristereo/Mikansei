package com.uragiristereo.mikansei.feature.user.settings.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.feature.user.settings.UserSettingsScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.userSettingsRoute(
    navController: NavHostController,
) {
    composable(
        route = UserRoute.Settings,
        disableDeserialization = true,
        content = {
            UserSettingsScreen(
                onNavigateBack = navController::navigateUp,
            )
        },
    )
}
