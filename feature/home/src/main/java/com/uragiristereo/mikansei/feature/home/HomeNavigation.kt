package com.uragiristereo.mikansei.feature.home

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.uragiristereo.mikansei.core.ui.animation.holdIn
import com.uragiristereo.mikansei.core.ui.animation.holdOut
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoutesString
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.routeOf
import com.uragiristereo.mikansei.feature.home.favorites.favoritesRoute
import com.uragiristereo.mikansei.feature.home.more.core.moreRoute
import com.uragiristereo.mikansei.feature.home.posts.core.postsRoute

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    onCurrentTagsChange: (String) -> Unit,
) {
    navigation<MainRoute.Home>(
        startDestination = HomeRoute.Posts(),
        enterTransition = {
            when (initialState.destination.id) {
                in HomeRoutesString -> fadeIn(animationSpec = tween(durationMillis = 300))
                routeOf<MainRoute.Search>() -> holdIn()
                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.id) {
                in HomeRoutesString -> fadeOut(animationSpec = tween(durationMillis = 300))
                routeOf<MainRoute.Search>() -> holdOut()
                routeOf<MainRoute.Image>() -> holdOut()
                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.id) {
                in HomeRoutesString -> fadeIn(animationSpec = tween(durationMillis = 300))
                routeOf<MainRoute.Search>() -> fadeIn()
                routeOf<MainRoute.Image>() -> holdIn()
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.id) {
                in HomeRoutesString -> fadeOut(animationSpec = tween(durationMillis = 300))
                routeOf<MainRoute.Search>() -> holdOut()
                routeOf<MainRoute.Image>() -> holdOut()
                else -> null
            }
        }
    ) {
        postsRoute(
            mainNavController = navController,
            onCurrentTagsChange = onCurrentTagsChange,
        )

        favoritesRoute(navController)

        moreRoute(navController)
    }
}
