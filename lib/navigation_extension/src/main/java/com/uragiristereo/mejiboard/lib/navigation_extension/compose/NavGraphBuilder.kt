package com.uragiristereo.mejiboard.lib.navigation_extension.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.uragiristereo.mejiboard.lib.navigation_extension.NavRoute
import com.uragiristereo.mejiboard.lib.navigation_extension.Serializer
import com.uragiristereo.mejiboard.lib.navigation_extension.getData
import com.uragiristereo.mejiboard.lib.navigation_extension.namedNavArg
import com.uragiristereo.mejiboard.lib.navigation_extension.parseRoute
import kotlinx.serialization.serializer
import kotlin.reflect.KClass


inline fun <reified T : NavRoute> NavGraphBuilder.composable(
    deepLinks: List<NavDeepLink> = listOf(),
    noinline content: @Composable T?.(NavBackStackEntry) -> Unit,
) {
    val route = T::class.java.getConstructor().newInstance()

    Serializer.addPolymorphicType(name = T::class.qualifiedName!!) {
        subclass(T::class, serializer())
    }

    composable(
        route = route.parseRoute(),
        arguments = listOf(namedNavArg),
        deepLinks = deepLinks,
        content = { entry ->
            val data = remember(entry) { route.getData<T>(entry) }

            content(data, entry)
        },
    )
}

inline fun <reified T : NavRoute> NavGraphBuilder.composable(
    route: KClass<T>,
    deepLinks: List<NavDeepLink> = listOf(),
    noinline content: @Composable T?.(NavBackStackEntry) -> Unit,
) {
    val newRoute = route.java.getConstructor().newInstance()

    Serializer.addPolymorphicType(name = T::class.qualifiedName!!) {
        subclass(T::class, serializer())
    }

    composable(
        route = newRoute.parseRoute(),
        arguments = listOf(namedNavArg),
        deepLinks = deepLinks,
        content = { entry ->
            val data = remember(entry) { newRoute.getData<T>(entry) }

            content(data, entry)
        },
    )
}

inline fun <reified T : NavRoute> NavGraphBuilder.composable(
    route: T,
    deepLinks: List<NavDeepLink> = listOf(),
    noinline content: @Composable T?.(NavBackStackEntry) -> Unit,
) {
    Serializer.addPolymorphicType(name = T::class.qualifiedName!!) {
        subclass(T::class, serializer())
    }

    composable(
        route = route.parseRoute(),
        arguments = listOf(namedNavArg),
        deepLinks = deepLinks,
        content = { entry ->
            val data = remember(entry) { route.getData<T>(entry) ?: route }

            content(data, entry)
        },
    )
}
