package com.uragiristereo.mejiboard.lib.navigation_extension

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import timber.log.Timber

@NoArg
interface NavRoute {
    val route: String
}

fun NavRoute.parseRoute(): String = "$route?data={data}"

internal fun NavRoute.parseData(): String {
    val encoded = Serializer.encode(value = this)

    return "$route?data=$encoded"
}

inline fun <reified T> NavRoute.getData(entry: NavBackStackEntry): T? {
    return when (val data = entry.arguments?.getString("data")) {
        null -> {
            val e = IllegalArgumentException("Navigation route \"${parseRoute()}\" cannot be found.")

            Timber.w(e.message)

            null
        }

        else -> Serializer.decode(data)
    }
}

val namedNavArg = navArgument(name = "data") {
    type = NavType.StringType
    nullable = true
    defaultValue = null
}
