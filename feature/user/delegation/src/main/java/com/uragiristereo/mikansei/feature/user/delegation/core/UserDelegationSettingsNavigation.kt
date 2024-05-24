package com.uragiristereo.mikansei.feature.user.delegation.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.feature.user.delegation.UserDelegationSettingsScreen
import com.uragiristereo.serializednavigationextension.navigation.compose.composable

fun NavGraphBuilder.userDelegationSettingsRoute(navController: NavHostController) {
    composable<UserRoute.DelegationSettings> {
        UserDelegationSettingsScreen(
            onNavigateBack = navController::navigateUp,
        )
    }
}
