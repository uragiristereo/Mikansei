package com.uragiristereo.mikansei.feature.user.deactivation.navigation

import com.uragiristereo.mikansei.core.ui.navigation.NavRoute
import kotlinx.serialization.Serializable

sealed interface UserDeactivationRoute : NavRoute {
    @Serializable
    data object Agreement : UserDeactivationRoute

    @Serializable
    data object Methods : UserDeactivationRoute

    @Serializable
    data object InApp : UserDeactivationRoute

    @Serializable
    data object InWeb : UserDeactivationRoute

    companion object {
        const val TOTAL_STEPS = 3
    }
}
