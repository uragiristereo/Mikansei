package com.uragiristereo.mikansei.feature.home

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.uragiristereo.mikansei.core.ui.animation.holdIn
import com.uragiristereo.mikansei.core.ui.animation.holdOut
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoutesString
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.home.favorites.favoritesRoute
import com.uragiristereo.mikansei.feature.home.more.core.moreRoute
import com.uragiristereo.mikansei.feature.home.posts.core.postsRoute
import com.uragiristereo.serializednavigationextension.navigation.compose.navigation
import com.uragiristereo.serializednavigationextension.runtime.routeOf

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
                routeOf<MainRoute.Search>() -> holdIn()
                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                in HomeRoutesString -> fadeOut(animationSpec = tween(durationMillis = 300))
                routeOf<MainRoute.Search>() -> holdOut()
                routeOf<MainRoute.Image>() -> holdOut()
                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                in HomeRoutesString -> fadeIn(animationSpec = tween(durationMillis = 300))
                routeOf<MainRoute.Search>() -> fadeIn()
                routeOf<MainRoute.Image>() -> holdIn()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                in HomeRoutesString -> fadeOut(animationSpec = tween(durationMillis = 300))
                routeOf<MainRoute.Search>() -> holdOut()
                routeOf<MainRoute.Image>() -> holdOut()
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
