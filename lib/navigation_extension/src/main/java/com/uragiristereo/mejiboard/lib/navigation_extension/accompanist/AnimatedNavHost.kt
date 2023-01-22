package com.uragiristereo.mejiboard.lib.navigation_extension.accompanist

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.uragiristereo.mejiboard.lib.navigation_extension.NavRoute
import com.uragiristereo.mejiboard.lib.navigation_extension.parseRoute
import kotlin.reflect.KClass

@OptIn(ExperimentalAnimationApi::class)
@Composable
inline fun <reified T : NavRoute> AnimatedNavHost(
    navController: NavHostController,
    startDestination: KClass<T>,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    route: String? = null,
    noinline enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition) =
        { fadeIn(animationSpec = tween(700)) },
    noinline exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition) =
        { fadeOut(animationSpec = tween(700)) },
    noinline popEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition) = enterTransition,
    noinline popExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition) = exitTransition,
    noinline builder: NavGraphBuilder.() -> Unit,
) {
    val startRoute = startDestination.java.getConstructor().newInstance()

    com.google.accompanist.navigation.animation.AnimatedNavHost(
        navController = navController,
        startDestination = startRoute.parseRoute(),
        modifier = modifier,
        contentAlignment = contentAlignment,
        route = route,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        builder = builder,
    )
}
