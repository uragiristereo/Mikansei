package com.uragiristereo.mikansei.feature.home.more.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.InterceptBackGestureForBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.feature.home.more.MoreScreen

fun NavGraphBuilder.moreRoute(mainNavController: NavHostController) {
    composable<HomeRoute.More> {
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
    }
}
