package com.uragiristereo.mikansei.feature.user

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.UserRoute
import com.uragiristereo.mikansei.feature.user.login.core.loginRoute
import com.uragiristereo.mikansei.feature.user.manage.core.manageRoute
import com.uragiristereo.mikansei.feature.user.settings.core.userSettingsRoute
import com.uragiristereo.serializednavigationextension.runtime.navigation

fun NavGraphBuilder.userGraph(
    navController: NavHostController,
) {
    navigation(
        startDestination = UserRoute.Login::class,
        route = MainRoute.User::class,
    ) {
        loginRoute(navController)

        manageRoute(navController)

        userSettingsRoute(navController)
    }
}
