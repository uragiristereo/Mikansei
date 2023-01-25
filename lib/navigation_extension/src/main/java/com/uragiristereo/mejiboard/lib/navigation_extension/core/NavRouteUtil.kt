package com.uragiristereo.mejiboard.lib.navigation_extension.core

import androidx.navigation.NavBackStackEntry
import timber.log.Timber

object NavRouteUtil {
    inline fun <reified T> getDataOrNull(
        route: NavRoute,
        entry: NavBackStackEntry,
    ): T? {
        return when (val data = entry.arguments?.getString("data")) {
            null -> {
                val e = IllegalArgumentException("Expecting navigation route data for \"${route.route}\" but got null!")

                Timber.w(e.message)

                null
            }

            else -> Serializer.decode(data)
        }
    }
}
