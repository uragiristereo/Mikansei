package com.uragiristereo.mejiboard.feature.home.posts

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import com.uragiristereo.mejiboard.core.ui.LocalPostsNavController
import com.uragiristereo.mejiboard.core.ui.navigation.MainRoute
import com.uragiristereo.mejiboard.core.ui.navigation.PostsRoute
import com.uragiristereo.mejiboard.lib.navigation_extension.accompanist.AnimatedNavHost
import com.uragiristereo.mejiboard.lib.navigation_extension.accompanist.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PostsNavGraph(
    onNavigateMain: (MainRoute) -> Unit,
    onNavigateImage: (Post) -> Unit,
    onCurrentTagsChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedNavHost(
        navController = LocalPostsNavController.current,
        startDestination = PostsRoute.Index::class,
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
            route = PostsRoute.Index::class,
            content = {
                PostsScreen(
                    onNavigate = onNavigateMain,
                    onNavigateImage = onNavigateImage,
                    onCurrentTagsChange = onCurrentTagsChange,
                )
            },
        )
    }
}
