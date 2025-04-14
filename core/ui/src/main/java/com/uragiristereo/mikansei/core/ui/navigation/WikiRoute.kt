package com.uragiristereo.mikansei.core.ui.navigation

import com.uragiristereo.serializednavigationextension.runtime.NavRoute
import kotlinx.serialization.Serializable

sealed interface WikiRoute : NavRoute {
    @Serializable
    data class Index(val tag: String = "help:home") : WikiRoute
}
