package com.uragiristereo.mikansei.feature.user.deactivation.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.feature.user.deactivation.UserDeactivationScreen
import com.uragiristereo.serializednavigationextension.navigation.compose.composable
import com.uragiristereo.serializednavigationextension.runtime.navigate
import com.uragiristereo.serializednavigationextension.runtime.routeOf

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
