package com.uragiristereo.mikansei.feature.user.login.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.feature.user.login.LoginScreen
import com.uragiristereo.serializednavigationextension.navigation.compose.composable

fun NavGraphBuilder.loginRoute(
    navController: NavHostController,
) {
    composable<UserRoute.Login> {
        LoginScreen(
            onNavigateBack = navController::navigateUp,
        )
    }
}
