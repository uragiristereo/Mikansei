package com.uragiristereo.mikansei.feature.about.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.about.AboutScreen
import com.uragiristereo.serializednavigationextension.navigation.compose.composable

fun NavGraphBuilder.aboutRoute(
    navController: NavHostController,
) {
    composable<MainRoute.About> {
        AboutScreen(
            onNavigateBack = navController::navigateUp,
        )
    }
}
