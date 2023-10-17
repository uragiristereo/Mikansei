package com.uragiristereo.mikansei.ui.navgraphs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.compose.NavHost
import com.uragiristereo.mikansei.core.ui.animation.translateXFadeIn
import com.uragiristereo.mikansei.core.ui.animation.translateXFadeOut
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.about.core.aboutRoute
import com.uragiristereo.mikansei.feature.filters.core.filtersRoute
import com.uragiristereo.mikansei.feature.home.homeGraph
import com.uragiristereo.mikansei.feature.image.core.imageRoute
import com.uragiristereo.mikansei.feature.search.core.searchRoute
import com.uragiristereo.mikansei.feature.settings.core.settingsGraph
import com.uragiristereo.mikansei.feature.user.userGraph
import com.uragiristereo.mikansei.ui.MainViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel(),
) {
    NavHost(
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
        )

        searchRoute(navController)

        imageRoute(
            navController = navController,
            navigatedBackByGesture = viewModel.navigatedBackByGesture,
            onNavigatedBackByGesture = viewModel::setNavigatedBackByGesture,
        )

        settingsGraph(navController)

        filtersRoute(navController)

        aboutRoute(navController)

        userGraph(navController)
    }
}
