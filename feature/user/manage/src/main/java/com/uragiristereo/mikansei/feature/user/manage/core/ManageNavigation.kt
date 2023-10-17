package com.uragiristereo.mikansei.feature.user.manage.core

import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.compose.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.feature.user.manage.ManageUserScreen
import com.uragiristereo.mikansei.feature.user.manage.switch_account.SwitchAccountContent

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

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.manageBottomRoute(mainNavController: NavHostController) {
    composable(UserRoute.Switch) {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        SwitchAccountContent(
            onDismiss = bottomSheetNavigator.bottomSheetState::hide,
            onNavigateToManage = {
                bottomSheetNavigator.runHiding {
                    mainNavController.navigate(UserRoute.Manage)
                }
            },
        )
    }
}
