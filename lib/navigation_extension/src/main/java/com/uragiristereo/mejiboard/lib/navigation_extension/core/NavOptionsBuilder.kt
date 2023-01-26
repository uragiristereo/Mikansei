package com.uragiristereo.mejiboard.lib.navigation_extension.core

import androidx.navigation.NavOptionsBuilder
import androidx.navigation.PopUpToBuilder
import kotlin.reflect.KClass

fun NavOptionsBuilder.popUpTo(
    route: NavRoute,
    popUpToBuilder: PopUpToBuilder.() -> Unit,
) {
    popUpTo(
        route = route.route,
        popUpToBuilder = popUpToBuilder,
    )
}

fun NavOptionsBuilder.popUpTo(
    route: KClass<NavRoute>,
    popUpToBuilder: PopUpToBuilder.() -> Unit,
) {
    popUpTo(
        route = route.route,
        popUpToBuilder = popUpToBuilder,
    )
}
