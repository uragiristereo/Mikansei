package com.uragiristereo.mikansei.feature.search.core

import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.AutoComplete
import com.uragiristereo.mikansei.core.ui.animation.holdIn
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoutesString
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.WikiRoute
import com.uragiristereo.mikansei.feature.search.SearchScreen
import com.uragiristereo.serializednavigationextension.navigation.compose.composable
import com.uragiristereo.serializednavigationextension.runtime.navigate
import com.uragiristereo.serializednavigationextension.runtime.routeOf

fun NavGraphBuilder.searchRoute(
    navController: NavHostController,
) {
    composable(
        defaultValue = MainRoute.Search(),
        enterTransition = {
            holdIn()
        },
        exitTransition = {
            when (targetState.destination.route) {
                in HomeRoutesString -> fadeOut()
                routeOf<WikiRoute.Index>() -> fadeOut()
                else -> null
            }
        },
        popEnterTransition = {
            null
        },
        popExitTransition = {
            when (targetState.destination.route) {
                in HomeRoutesString -> fadeOut()
                routeOf<WikiRoute.Index>() -> fadeOut()
                else -> null
            }
        }
    ) {
        val args = rememberNavArgsOf()

        SearchScreen(
            onNavigateBack = navController::navigateUp,
            onSearchSubmit = { tags ->
                when (args.searchType) {
                    AutoComplete.SearchType.TAG_QUERY -> {
                        navController.navigate(route = HomeRoute.Posts(tags)) {
                            popUpTo(navController.graph.findStartDestination().id)
                        }
                    }

                    AutoComplete.SearchType.WIKI_PAGE -> {
                        navController.navigate(route = WikiRoute.Index(tags)) {
                            popUpTo(routeOf<MainRoute.Search>()) {
                                inclusive = true
                            }
                        }
                    }
                }
            },
        )
    }
}
