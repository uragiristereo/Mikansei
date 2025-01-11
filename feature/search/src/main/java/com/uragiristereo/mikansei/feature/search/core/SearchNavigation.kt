package com.uragiristereo.mikansei.feature.search.core

import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.uragiristereo.mikansei.core.ui.animation.holdIn
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoutesString
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.search.SearchScreen

fun NavGraphBuilder.searchRoute(
    navController: NavHostController,
) {
    composable<MainRoute.Search>(
        enterTransition = {
            holdIn()
        },
        exitTransition = {
            when (targetState.destination.id) {
                in HomeRoutesString -> fadeOut()
                else -> null
            }
        },
        popEnterTransition = {
            null
        },
        popExitTransition = {
            when (targetState.destination.id) {
                in HomeRoutesString -> fadeOut()
                else -> null
            }
        }
    ) {
        SearchScreen(
            onNavigateBack = navController::navigateUp,
            onSearchSubmit = { tags ->
                navController.navigate(route = HomeRoute.Posts(tags)) {
                    popUpTo(navController.graph.findStartDestination().id)
                }
            },
        )
    }
}
