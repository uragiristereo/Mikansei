package com.uragiristereo.mejiboard.presentation.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.uragiristereo.mejiboard.domain.entity.source.post.Post
import com.uragiristereo.mejiboard.presentation.common.LocalHomeNavController
import com.uragiristereo.mejiboard.presentation.common.navigation.NavigationRoute
import com.uragiristereo.mejiboard.presentation.common.navigation.composable
import com.uragiristereo.mejiboard.presentation.home.route.collections.CollectionsScreen
import com.uragiristereo.mejiboard.presentation.home.route.more.MoreScreen
import com.uragiristereo.mejiboard.presentation.home.route.posts.PostsNavGraph

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeNavGraph(
    onNavigate: (NavigationRoute) -> Unit,
    onNavigateImage: (Post) -> Unit,
    onCurrentTagsChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedNavHost(
        navController = LocalHomeNavController.current,
        startDestination = HomeRoute.Posts.route,
        enterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = 300))
        },
        modifier = modifier,
    ) {
        // Posts
        composable(
            route = HomeRoute.Posts,
            content = {
                PostsNavGraph(
                    onNavigate = onNavigate,
                    onNavigateImage = onNavigateImage,
                    onCurrentTagsChange = onCurrentTagsChange,
                )
            },
        )

        // Collections
        composable(
            route = HomeRoute.Collections,
            content = {
                CollectionsScreen()
            },
        )

        // More
        composable(
            route = HomeRoute.More,
            content = {
                MoreScreen(
                    onNavigate = onNavigate,
                )
            },
        )
    }
}
