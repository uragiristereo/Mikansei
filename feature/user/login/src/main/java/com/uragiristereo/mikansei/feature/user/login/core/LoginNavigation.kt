package com.uragiristereo.mikansei.feature.user.login.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.compose.composable
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.feature.user.login.LoginScreen

fun NavGraphBuilder.loginRoute(
    navController: NavHostController,
) {
    composable(
        route = UserRoute.Login,
    ) {
        LoginScreen(
            onNavigateBack = navController::navigateUp,
        )
    }
}
