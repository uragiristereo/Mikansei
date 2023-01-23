package com.uragiristereo.mejiboard.lib.navigation_extension

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import timber.log.Timber
import kotlin.reflect.KClass

@NoArg
interface NavRoute {
    val route: String
}

val NavRoute.parsedRoute: String
    get() = "$route?data={data}"

val <T : NavRoute> KClass<T>.route: String
    get() = this.java.getConstructor().newInstance().parsedRoute

internal fun NavRoute.parseData(): String {
    val encoded = Serializer.encode(value = this)

    return "$route?data=$encoded"
}

inline fun <reified T> NavRoute.getData(entry: NavBackStackEntry): T? {
    return when (val data = entry.arguments?.getString("data")) {
        null -> {
            val e = IllegalArgumentException("Navigation route \"$parsedRoute\" cannot be found.")

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
