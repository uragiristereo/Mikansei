package com.uragiristereo.mikansei.feature.home.more.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.InterceptBackGestureForBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.feature.home.more.MoreScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.moreRoute(mainNavController: NavHostController) {
    composable(
        route = HomeRoute.More,
        content = {
            val bottomSheetNavigator = LocalBottomSheetNavigator.current

            InterceptBackGestureForBottomSheetNavigator()

            MoreScreen(
                onNavigate = mainNavController::navigate,
                onNavigateBottom = { route ->
                    bottomSheetNavigator.navigate {
                        it.navigate(route)
                    }
                },
            )
        },
    )
}
