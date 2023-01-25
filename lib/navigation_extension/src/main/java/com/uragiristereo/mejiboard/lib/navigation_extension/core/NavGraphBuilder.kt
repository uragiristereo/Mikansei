package com.uragiristereo.mejiboard.lib.navigation_extension.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

inline fun <reified T : NavRoute> NavGraphBuilder.dialog(
    route: T,
    deepLinks: List<NavDeepLink> = listOf(),
    noinline content: @Composable T.(NavBackStackEntry) -> Unit,
) {
    Serializer.addPolymorphicType(name = T::class.qualifiedName!!) {
        subclass(T::class, serializer())
    }

    dialog(
        route = route.route,
        arguments = listOf(namedNavArg),
        deepLinks = deepLinks,
        content = { entry ->
            val data = remember(entry) {
                when (val data = entry.arguments?.getString("data")) {
                    null -> route

                    else -> Serializer.decode(data) ?: route
                }
            }

            content(data, entry)
        },
    )
}

@Suppress("UNUSED_PARAMETER")
inline fun <reified T : NavRoute> NavGraphBuilder.dialog(
    route: T,
    disableDeserialization: Boolean,
    deepLinks: List<NavDeepLink> = listOf(),
    noinline content: @Composable NavBackStackEntry.() -> Unit,
) {
    Serializer.addPolymorphicType(name = T::class.qualifiedName!!) {
        subclass(T::class, serializer())
    }

    dialog(
        route = route.route,
        arguments = listOf(namedNavArg),
        deepLinks = deepLinks,
        content = { entry ->
            content(entry)
        },
    )
}

inline fun <reified T : NavRoute> NavGraphBuilder.dialog(
    route: KClass<T>,
    deepLinks: List<NavDeepLink> = listOf(),
    noinline content: @Composable NavBackStackEntry.(T?) -> Unit,
) {
    val newRoute = route.java.getConstructor().newInstance()

    Serializer.addPolymorphicType(name = T::class.qualifiedName!!) {
        subclass(T::class, serializer())
    }

    dialog(
        route = newRoute.route,
        arguments = listOf(namedNavArg),
        deepLinks = deepLinks,
        content = { entry ->
            val data = remember(entry) { NavRouteUtil.getDataOrNull<T>(newRoute, entry) }

            content(entry, data)
        },
    )
}

@Suppress("UNUSED_PARAMETER")
inline fun <reified T : NavRoute> NavGraphBuilder.dialog(
    route: KClass<T>,
    disableDeserialization: Boolean,
    deepLinks: List<NavDeepLink> = listOf(),
    noinline content: @Composable NavBackStackEntry.() -> Unit,
) {
    val newRoute = route.java.getConstructor().newInstance()

    Serializer.addPolymorphicType(name = T::class.qualifiedName!!) {
        subclass(T::class, serializer())
    }

    dialog(
        route = newRoute.route,
        arguments = listOf(namedNavArg),
        deepLinks = deepLinks,
        content = { entry ->
            content(entry)
        },
    )
}
