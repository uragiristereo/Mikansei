package com.uragiristereo.mikansei.feature.home.more.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.feature.home.more.MoreScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.moreRoute(
    navController: NavHostController,
) {
    composable(
        route = HomeRoute.More,
        content = {
            MoreScreen(onNavigate = navController::navigate)
        },
    )
}
