package com.uragiristereo.mikansei.core.ui.navigation

import com.github.uragiristereo.safer.compose.navigation.core.NavRoute

sealed interface UserRoute : NavRoute {
    object Login : UserRoute

    object Manage : UserRoute

    object Settings : UserRoute

    object Switch : UserRoute
}
