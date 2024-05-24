package com.uragiristereo.mikansei.core.ui.navigation

import com.uragiristereo.serializednavigationextension.runtime.NavRoute
import kotlinx.serialization.Serializable

sealed interface UserRoute : NavRoute {
    @Serializable
    data object Login : UserRoute

    @Serializable
    data object Manage : UserRoute

    @Serializable
    data object Settings : UserRoute

    @Serializable
    data object Switch : UserRoute

    @Serializable
    data object DelegationSettings : UserRoute
}
