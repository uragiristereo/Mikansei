package com.uragiristereo.mejiboard.feature.home.posts

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.uragiristereo.mejiboard.core.common.ui.LocalPostsNavController
import com.uragiristereo.mejiboard.core.model.navigation.NavigationRoute
import com.uragiristereo.mejiboard.core.model.navigation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PostsNavGraph(
    onNavigate: (NavigationRoute) -> Unit,
    onNavigateImage: (com.uragiristereo.mejiboard.core.model.booru.post.Post) -> Unit,
    onCurrentTagsChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedNavHost(
        navController = LocalPostsNavController.current,
        startDestination = PostsRoute.Index.route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = 350),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = 350),
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = 350),
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = 350),
            )
        },
        modifier = modifier,
    ) {
        composable(
            route = PostsRoute.Index,
            content = {
                PostsScreen(
                    onNavigate = onNavigate,
                    onNavigateImage = onNavigateImage,
                    onCurrentTagsChange = onCurrentTagsChange,
                )
            },
        )
    }
}
