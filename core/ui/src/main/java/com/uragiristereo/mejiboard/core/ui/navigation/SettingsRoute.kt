package com.uragiristereo.mejiboard.core.ui.navigation

import com.github.uragiristereo.safer.compose.navigation.core.NavRoute

sealed interface SettingsRoute : NavRoute {
    object Index : NavRoute
}
