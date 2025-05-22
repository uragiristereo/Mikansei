package com.uragiristereo.mikansei.feature.user.manage.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.InterceptBackGestureForBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.feature.user.manage.ManageUserScreen
import com.uragiristereo.mikansei.feature.user.manage.switch_account.SwitchAccountContent

fun NavGraphBuilder.manageRoute(
    navController: NavHostController,
) {
    composable<UserRoute.Manage> {
        ManageUserScreen(
            onNavigate = navController::navigate,
            onNavigateBack = navController::navigateUp,
        )
    }
}

fun NavGraphBuilder.manageBottomRoute(mainNavController: NavHostController) {
    composable<UserRoute.Switch> {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        InterceptBackGestureForBottomSheetNavigator()

        SwitchAccountContent(
            onDismiss = bottomSheetNavigator::hideSheet,
            onNavigateToManage = {
                bottomSheetNavigator.runHiding {
                    mainNavController.navigate(UserRoute.Manage)
                }
            },
        )
    }
}
