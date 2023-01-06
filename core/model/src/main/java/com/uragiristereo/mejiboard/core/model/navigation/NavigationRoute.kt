package com.uragiristereo.mejiboard.core.model.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import timber.log.Timber

open class NavigationRoute(
    route: String,
    private val argsKeys: List<String> = listOf(),
    private val navigationDebugLevel: NavigationDebugLevel = NavigationDebugLevel.MESSAGE,
) {
    val route = parseRoute(route, argsKeys)

    override fun toString(): String {
        return route
    }

    private fun parseRoute(route: String, keys: List<String>): String {
        var args = ""

        keys.forEach { key ->
            args += "&$key={$key}"
        }

        if (args.take(n = 1) == "&") {
            args = args.replaceFirst(oldChar = '&', newChar = '?')
        }

        return route + args
    }

    fun getNamedNavArgs(): List<NamedNavArgument> {
        return argsKeys.map { key ->
            navArgument(name = key) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        }
    }

    fun parseData(params: Map<String, Any>): String {
        var result = route

        params.forEach { (key, value) ->
            val arg = value.toJsonBase64Encoded()

            result = result.replace(oldValue = "{$key}", newValue = arg)
        }

        return result
    }

    fun printNavigationError(e: Throwable) {
        when (navigationDebugLevel) {
            NavigationDebugLevel.STACKTRACE -> e.printStackTrace()
            NavigationDebugLevel.MESSAGE -> Timber.e("${e.message}")
            NavigationDebugLevel.DISABLED -> {}
        }
    }

    inline fun <reified T> getData(entry: NavBackStackEntry, key: String): T? {
        return when (val dataStr = entry.arguments?.getString(key)) {
            null -> {
                val e = IllegalArgumentException("Navigation route \"$route\" data with arg key \"$key\" cannot be found.")

                printNavigationError(e)

                null
            }

            else -> dataStr.fromJsonBase64Encoded()
        }
    }

    @Composable
    inline fun <reified T> rememberGetData(key: String): T? {
        val entry = LocalNavBackStackEntry.current

        return remember(entry) { getData(entry, key) }
    }

    @Composable
    inline fun <reified T> rememberGetData(key: String, defaultValue: T): T {
        val entry = LocalNavBackStackEntry.current

        return remember(entry) { getData(entry, key) ?: defaultValue }
    }
}
