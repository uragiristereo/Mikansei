package com.uragiristereo.mejiboard.feature.search.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.uragiristereo.mejiboard.core.ui.animation.holdIn
import com.uragiristereo.mejiboard.core.ui.navigation.HomeRoute
import com.uragiristereo.mejiboard.core.ui.navigation.HomeRoutesString
import com.uragiristereo.mejiboard.core.ui.navigation.MainRoute
import com.uragiristereo.mejiboard.feature.search.SearchScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.searchRoute(
    navController: NavHostController,
) {
    composable(
        route = MainRoute.Search(),
        disableDeserialization = true,
        enterTransition = {
            holdIn()
        },
        exitTransition = {
            when (targetState.destination.route) {
                in HomeRoutesString -> fadeOut()
                else -> null
            }
        },
        popEnterTransition = {
            null
        },
        popExitTransition = {
            when (targetState.destination.route) {
                in HomeRoutesString -> fadeOut()
                else -> null
            }
        },
        content = {
            SearchScreen(
                onNavigate = navController::navigate,
                onNavigateBack = navController::navigateUp,
                onSearchSubmit = { tags ->
                    navController.popBackStack()

                    navController.navigate(
                        route = HomeRoute.Posts(tags),
                    )
                },
            )
        }
    )
}