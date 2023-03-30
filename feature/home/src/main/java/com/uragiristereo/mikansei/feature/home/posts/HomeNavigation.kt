package com.uragiristereo.mikansei.feature.home.posts

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.navigation
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.uragiristereo.mikansei.core.ui.animation.holdIn
import com.uragiristereo.mikansei.core.ui.animation.holdOut
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoutesString
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.home.collections.core.collectionsRoute
import com.uragiristereo.mikansei.feature.home.more.core.moreRoute
import com.uragiristereo.mikansei.feature.home.posts.core.postsRoute

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    onNavigatedBackByGesture: (Boolean) -> Unit,
    onCurrentTagsChange: (String) -> Unit,
) {
    navigation(
        startDestination = HomeRoute.Posts::class,
        route = MainRoute.Home::class,
        enterTransition = {
            when (initialState.destination.route) {
                in HomeRoutesString -> fadeIn(animationSpec = tween(durationMillis = 300))
                MainRoute.Search::class.route -> fadeIn()
                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                in HomeRoutesString -> fadeOut(animationSpec = tween(durationMillis = 300))
                MainRoute.Search::class.route -> holdOut()
                MainRoute.Image::class.route -> holdOut(durationMillis = 350)
                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                in HomeRoutesString -> fadeIn(animationSpec = tween(durationMillis = 300))
                MainRoute.Search::class.route -> fadeIn()
                MainRoute.Image::class.route -> holdIn(durationMillis = 350)
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                in HomeRoutesString -> fadeOut(animationSpec = tween(durationMillis = 300))
                MainRoute.Search::class.route -> holdOut()
                MainRoute.Image::class.route -> holdOut(durationMillis = 350)
                else -> null
            }
        }
    ) {
        postsRoute(
            navController = navController,
            onNavigatedBackByGesture = onNavigatedBackByGesture,
            onCurrentTagsChange = onCurrentTagsChange,
        )

        collectionsRoute()

        moreRoute(navController)
    }
}
