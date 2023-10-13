package com.uragiristereo.mikansei.feature.user.manage.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigation.ExperimentalMaterialNavigationApi
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigation.bottomSheet
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.feature.user.manage.ManageUserScreen
import com.uragiristereo.mikansei.feature.user.manage.switch_account.SwitchAccountContent

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
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

    bottomSheet(
        route = UserRoute.Switch.route,
    ) {
        SwitchAccountContent(
            onNavigateBack = navController::navigateUp,
            onNavigateToManage = {
                navController.navigate(UserRoute.Manage)
            },
        )
    }
}
