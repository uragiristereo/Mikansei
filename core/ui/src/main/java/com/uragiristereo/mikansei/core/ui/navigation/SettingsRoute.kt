package com.uragiristereo.mikansei.core.ui.navigation

import kotlinx.serialization.Serializable

sealed interface SettingsRoute : NavRoute {
    @Serializable
    data object Index : SettingsRoute
}
