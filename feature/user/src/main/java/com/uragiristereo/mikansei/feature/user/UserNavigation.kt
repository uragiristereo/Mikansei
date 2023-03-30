package com.uragiristereo.mikansei.feature.user

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.navigation
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.userGraph(
    navController: NavHostController,
) {
    navigation(
        startDestination = UserRoute.Login::class,
        route = MainRoute.User::class,
    ) {

    }
}
