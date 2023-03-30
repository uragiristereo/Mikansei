package com.uragiristereo.mikansei.feature.home.posts.core

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.uragiristereo.mikansei.core.ui.LocalNavigationRailPadding
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.home.posts.PostsScreen

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.postsRoute(
    navController: NavHostController,
    onNavigatedBackByGesture: (Boolean) -> Unit,
    onCurrentTagsChange: (String) -> Unit,
) {
    composable(
        route = HomeRoute.Posts(),
        enterTransition = {
            when (initialState.destination.route) {
                HomeRoute.Posts::class.route -> slideIntoContainer(
                    towards = AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 350),
                )

                else -> null
            }

        },
        exitTransition = {
            when (targetState.destination.route) {
                HomeRoute.Posts::class.route -> slideOutOfContainer(
                    towards = AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 350),
                )

                else -> null
            }
        },
        content = { data ->
            LaunchedEffect(key1 = data.tags) {
                onCurrentTagsChange(data.tags)
            }

            PostsScreen(
                onNavigate = navController::navigate,
                onNavigateImage = { item ->
                    onNavigatedBackByGesture(false)

                    navController.navigate(
                        MainRoute.Image(post = item)
                    )
                },
                modifier = Modifier.padding(start = LocalNavigationRailPadding.current),
            )
        },
    )
}
