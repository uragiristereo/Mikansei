package com.uragiristereo.mikansei.feature.home

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.compose.navigation
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.uragiristereo.mikansei.core.ui.animation.holdIn
import com.uragiristereo.mikansei.core.ui.animation.holdOut
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoutesString
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.home.favorites.favoritesRoute
import com.uragiristereo.mikansei.feature.home.more.core.moreRoute
import com.uragiristereo.mikansei.feature.home.posts.core.postsRoute

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
                MainRoute.Search::class.route -> holdIn()
                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                in HomeRoutesString -> fadeOut(animationSpec = tween(durationMillis = 300))
                MainRoute.Search::class.route -> holdOut()
                MainRoute.Image::class.route -> holdOut()
                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                in HomeRoutesString -> fadeIn(animationSpec = tween(durationMillis = 300))
                MainRoute.Search::class.route -> fadeIn()
                MainRoute.Image::class.route -> holdIn()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                in HomeRoutesString -> fadeOut(animationSpec = tween(durationMillis = 300))
                MainRoute.Search::class.route -> holdOut()
                MainRoute.Image::class.route -> holdOut()
                else -> null
            }
        }
    ) {
        postsRoute(
            mainNavController = navController,
            onNavigatedBackByGesture = onNavigatedBackByGesture,
            onCurrentTagsChange = onCurrentTagsChange,
        )

        favoritesRoute(navController)

        moreRoute(navController)
    }
}
