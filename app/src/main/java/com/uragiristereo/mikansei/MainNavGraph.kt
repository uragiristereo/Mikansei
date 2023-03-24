package com.uragiristereo.mikansei

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.AnimatedNavHost
import com.uragiristereo.mikansei.core.ui.animation.translateXFadeIn
import com.uragiristereo.mikansei.core.ui.animation.translateXFadeOut
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.about.core.aboutRoute
import com.uragiristereo.mikansei.feature.filters.core.filtersRoute
import com.uragiristereo.mikansei.feature.home.posts.homeGraph
import com.uragiristereo.mikansei.feature.image.core.imageRoute
import com.uragiristereo.mikansei.feature.search.core.searchRoute
import com.uragiristereo.mikansei.feature.search_history.core.searchHistoryRoute
import com.uragiristereo.mikansei.feature.settings.core.settingsGraph
import com.uragiristereo.mikansei.saved_searches.core.savedSearchesRoute
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel(),
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = MainRoute.Home::class,
        enterTransition = {
            translateXFadeIn(forward = true)
        },
        exitTransition = {
            translateXFadeOut(forward = true)
        },
        popEnterTransition = {
            translateXFadeIn(forward = false)
        },
        popExitTransition = {
            translateXFadeOut(forward = false)
        },
        modifier = modifier.fillMaxSize(),
    ) {
        homeGraph(
            navController = navController,
            onNavigatedBackByGesture = viewModel::setNavigatedBackByGesture,
            onCurrentTagsChange = viewModel::setCurrentTags,
            onRequestScrollToTop = viewModel::setScrollToTopCallback,
        )

        searchRoute(navController)

        imageRoute(
            navController = navController,
            navigatedBackByGesture = viewModel.navigatedBackByGesture,
            onNavigatedBackByGesture = viewModel::setNavigatedBackByGesture,
        )

        settingsGraph(navController)

        filtersRoute(navController)

        savedSearchesRoute(navController)

        searchHistoryRoute(navController)

        aboutRoute(navController)
    }
}
