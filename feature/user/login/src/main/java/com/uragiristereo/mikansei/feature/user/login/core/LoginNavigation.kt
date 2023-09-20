package com.uragiristereo.mikansei.feature.user.login.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.feature.user.login.LoginScreen

@OptIn(ExperimentalAnimationApi::class)
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
