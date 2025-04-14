package com.uragiristereo.mikansei.feature.wiki

import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.AutoComplete
import com.uragiristereo.mikansei.core.ui.animation.holdIn
import com.uragiristereo.mikansei.core.ui.animation.holdOut
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.WikiRoute
import com.uragiristereo.serializednavigationextension.navigation.compose.composable
import com.uragiristereo.serializednavigationextension.runtime.navigate
import com.uragiristereo.serializednavigationextension.runtime.navigation
import com.uragiristereo.serializednavigationextension.runtime.routeOf
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val wikiModule = module {
    viewModelOf(::WikiViewModel)
}

fun NavGraphBuilder.wikiGraph(navController: NavHostController) {
    navigation(
        route = MainRoute.Wiki::class,
        startDestination = WikiRoute.Index::class,
    ) {
        wikiIndexRoute(navController)
    }
}

private fun NavGraphBuilder.wikiIndexRoute(navController: NavHostController) {
    composable(
        defaultValue = WikiRoute.Index(),
        enterTransition = {
            when (initialState.destination.route) {
                routeOf<MainRoute.Search>() -> holdIn()
                routeOf<MainRoute.Image>() -> holdIn()
                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                routeOf<MainRoute.Search>() -> fadeOut()
                routeOf<MainRoute.Image>() -> holdOut()
                else -> null
            }
        },
    ) {
        WikiScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToSearch = {
                navController.navigate(
                    MainRoute.Search(searchType = AutoComplete.SearchType.WIKI_PAGE)
                )
            },
            onNavigateToWiki = { tag ->
                navController.navigate(WikiRoute.Index(tag))
            },
            onNavigateToViewer = { post ->
                navController.navigate(MainRoute.Image(post))
            },
            onNavigateToPosts = { tags ->
                navController.navigate(HomeRoute.Posts("$tags "))
            },
        )
    }
}
