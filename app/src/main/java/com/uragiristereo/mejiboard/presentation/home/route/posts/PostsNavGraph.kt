package com.uragiristereo.mejiboard.presentation.home.route.posts

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.uragiristereo.mejiboard.domain.entity.source.post.Post
import com.uragiristereo.mejiboard.presentation.common.LocalPostsNavController
import com.uragiristereo.mejiboard.presentation.common.navigation.NavigationRoute
import com.uragiristereo.mejiboard.presentation.common.navigation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PostsNavGraph(
    onNavigate: (NavigationRoute) -> Unit,
    onNavigateImage: (Post) -> Unit,
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
                val tags = rememberGetData(key = "tags", defaultValue = "")

                PostsScreen(
                    tags = tags,
                    onNavigate = onNavigate,
                    onNavigateImage = onNavigateImage,
                    onCurrentTagsChange = onCurrentTagsChange,
                )
            },
        )
    }
}
