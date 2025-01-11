package com.uragiristereo.mikansei.feature.user.delegation.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.feature.user.delegation.UserDelegationSettingsScreen

fun NavGraphBuilder.userDelegationSettingsRoute(navController: NavHostController) {
    composable<UserRoute.DelegationSettings> {
        UserDelegationSettingsScreen(
            onNavigateBack = navController::navigateUp,
        )
    }
}
