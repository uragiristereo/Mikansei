package com.uragiristereo.mejiboard.feature.home.more.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.uragiristereo.mejiboard.core.ui.LocalNavigationRailPadding
import com.uragiristereo.mejiboard.core.ui.navigation.HomeRoute
import com.uragiristereo.mejiboard.feature.home.more.MoreScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.moreRoute(
    navController: NavHostController,
) {
    composable(
        route = HomeRoute.More,
        content = {
            MoreScreen(
                onNavigate = navController::navigate,
                modifier = Modifier.padding(start = LocalNavigationRailPadding.current),
            )
        },
    )
}
