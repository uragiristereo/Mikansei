package com.uragiristereo.mejiboard.core.ui.navigation

import com.github.uragiristereo.safer.compose.navigation.core.NavRoute
import kotlinx.serialization.Serializable

sealed interface SettingsRoute : NavRoute {
    @Serializable
    object Index : NavRoute {
        override val route: String = "settings/index"
    }
}
