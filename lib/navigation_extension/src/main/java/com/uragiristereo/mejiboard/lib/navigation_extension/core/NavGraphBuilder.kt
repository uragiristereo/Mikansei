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
    route: KClass<T>,
    deepLinks: List<NavDeepLink> = listOf(),
    noinline content: @Composable T?.(NavBackStackEntry) -> Unit,
) {
    val newRoute = route.java.getConstructor().newInstance()

    Serializer.addPolymorphicType(name = T::class.qualifiedName!!) {
        subclass(T::class, serializer())
    }

    dialog(
        route = newRoute.parsedRoute,
        arguments = listOf(namedNavArg),
        deepLinks = deepLinks,
        content = { entry ->
            val data = remember(entry) { newRoute.getData<T>(entry) }

            content(data, entry)
        },
    )
}
