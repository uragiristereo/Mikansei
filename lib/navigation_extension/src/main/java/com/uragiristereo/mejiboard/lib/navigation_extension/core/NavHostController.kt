package com.uragiristereo.mejiboard.lib.navigation_extension.core

import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.navOptions
import timber.log.Timber

fun NavHostController.navigate(
    route: NavRoute,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    val parsedRoute = when {
        route.route.contains(other = "?data={data}") -> route.parseData()
        else -> route.route
    }

    val uri = NavDestination.createRoute(parsedRoute).toUri()

    try {
        navigate(
            request = NavDeepLinkRequest.Builder
                .fromUri(uri)
                .build(),
            navOptions = navOptions,
            navigatorExtras = navigatorExtras,
        )
    } catch (e: IllegalArgumentException) {
        // When the data is too large it usually throws IllegalArgumentException "Navigation destination that matches request cannot be found"
        // So we're printing the error instead

        Timber.e(e.message)
    }
}

fun NavHostController.navigate(
    route: NavRoute,
    builder: NavOptionsBuilder.() -> Unit,
) {
    navigate(
        route = route,
        navOptions = navOptions(builder),
    )
}
