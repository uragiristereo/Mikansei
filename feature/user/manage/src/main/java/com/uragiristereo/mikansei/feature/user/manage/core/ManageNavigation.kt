package com.uragiristereo.mikansei.feature.user.manage.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.feature.user.manage.ManageUserScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.manageRoute(
    navController: NavHostController,
) {
    composable(
        route = UserRoute.Manage,
    ) {
        ManageUserScreen(
            onNavigate = navController::navigate,
            onNavigateBack = navController::navigateUp,
        )
    }
}
