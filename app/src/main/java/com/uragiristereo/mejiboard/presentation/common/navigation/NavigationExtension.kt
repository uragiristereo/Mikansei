package com.uragiristereo.mejiboard.presentation.common.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.navOptions
import com.google.accompanist.navigation.animation.composable

val LocalNavBackStackEntry = compositionLocalOf<NavBackStackEntry> { error(message = "no NavBackStackEntry provided!") }

fun NavHostController.navigate(
    route: NavigationRoute,
    data: Map<String, Any> = mapOf(),
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    val uri = route.parseData(data)

    try {
        navigate(
            request = NavDeepLinkRequest.Builder
                .fromUri(NavDestination.createRoute(uri).toUri())
                .build(),
            navOptions = navOptions,
            navigatorExtras = navigatorExtras,
        )
    } catch (e: IllegalArgumentException) {
        // When the data is too large it usually throws IllegalArgumentException "Navigation destination that matches request cannot be found"
        // So we're printing the error instead

        route.printNavigationError(e)
    }
}

fun NavHostController.navigate(
    route: NavigationRoute,
    data: Map<String, Any> = mapOf(),
    builder: NavOptionsBuilder.() -> Unit,
) {
    navigate(
        route = route,
        data = data,
        navOptions = navOptions(builder),
    )
}

inline fun <reified T> SavedStateHandle.getData(key: String): T? {
    return this.get<String>(key)?.fromJsonBase64Encoded()
}

inline fun <reified T> SavedStateHandle.getData(key: String, defaultValue: T): T {
    return this.get<String>(key)?.fromJsonBase64Encoded() ?: defaultValue
}

//fun NavGraphBuilder.composable(
//    route: NavigationRoute,
//    deepLinks: List<NavDeepLink> = listOf(),
//    content: @Composable NavigationRoute.(NavBackStackEntry) -> Unit,
//) {
//    composable(
//        route = route.route,
//        arguments = route.getNamedNavArgs(),
//        deepLinks = deepLinks,
//        content = { entry ->
//            CompositionLocalProvider(
//                values = arrayOf(
//                    LocalNavBackStackEntry provides entry,
//                ),
//                content = { content(route) },
//            )
//        },
//    )
//}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.composable(
    route: NavigationRoute,
    deepLinks: List<NavDeepLink> = listOf(),
    enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    popEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
    popExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition,
    content: @Composable NavigationRoute.() -> Unit,
) {
    composable(
        route = route.route,
        arguments = route.getNamedNavArgs(),
        deepLinks = deepLinks,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        content = { entry ->
            CompositionLocalProvider(
                values = arrayOf(
                    LocalNavBackStackEntry provides entry,
                ),
                content = { content(route) },
            )
        },
    )
}
