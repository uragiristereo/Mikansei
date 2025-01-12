package com.uragiristereo.mikansei.feature.user.deactivation.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.core.ui.navigation.routeOf
import com.uragiristereo.mikansei.feature.user.deactivation.UserDeactivationScreen

fun NavGraphBuilder.userDeactivationRoute(
    navController: NavHostController,
) {
    composable<UserRoute.Deactivation> {
        UserDeactivationScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateMore = {
                navController.navigate(HomeRoute.More) {
                    popUpTo(routeOf<HomeRoute.More>())
                }
            },
        )
    }
}
