package com.uragiristereo.mejiboard.lib.navigation_extension.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.uragiristereo.mejiboard.lib.navigation_extension.NavRoute
import com.uragiristereo.mejiboard.lib.navigation_extension.parsedRoute
import kotlin.reflect.KClass

@Composable
inline fun <reified T : NavRoute> NavHost(
    navController: NavHostController,
    startDestination: KClass<T>,
    modifier: Modifier = Modifier,
    route: String? = null,
    noinline builder: NavGraphBuilder.() -> Unit,
) {
    val startRoute = startDestination.java.getConstructor().newInstance()

    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = startRoute.parsedRoute,
        modifier = modifier,
        route = route,
        builder = builder,
    )
}
