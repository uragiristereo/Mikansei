package com.uragiristereo.mikansei.feature.home.more.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.uragiristereo.mikansei.core.ui.LocalNavigationRailPadding
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.feature.home.more.MoreScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.moreRoute(
    navController: NavHostController,
) {
    composable(
        route = HomeRoute.More,
        content = {
            MoreScreen(
                onNavigate = navController::navigate,
                modifier = Modifier
                    .padding(start = LocalNavigationRailPadding.current)
                    .displayCutoutPadding()
                    .windowInsetsPadding(
                        WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal)
                    ),
            )
        },
    )
}
