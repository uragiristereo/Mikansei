package com.uragiristereo.mejiboard.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.AnimatedNavHost
import com.uragiristereo.mejiboard.core.ui.animation.translateXFadeIn
import com.uragiristereo.mejiboard.core.ui.animation.translateXFadeOut
import com.uragiristereo.mejiboard.core.ui.navigation.MainRoute
import com.uragiristereo.mejiboard.feature.about.core.aboutRoute
import com.uragiristereo.mejiboard.feature.filters.core.filtersRoute
import com.uragiristereo.mejiboard.feature.home.posts.homeGraph
import com.uragiristereo.mejiboard.feature.image.core.imageRoute
import com.uragiristereo.mejiboard.feature.search.core.searchRoute
import com.uragiristereo.mejiboard.feature.search_history.core.searchHistoryRoute
import com.uragiristereo.mejiboard.feature.settings.core.settingsGraph
import com.uragiristereo.mejiboard.saved_searches.core.savedSearchesRoute
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
