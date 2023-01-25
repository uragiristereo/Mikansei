package com.uragiristereo.mejiboard.feature.home.posts

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import com.uragiristereo.mejiboard.core.ui.LocalHomeNavController
import com.uragiristereo.mejiboard.core.ui.navigation.HomeRoute
import com.uragiristereo.mejiboard.core.ui.navigation.MainRoute
import com.uragiristereo.mejiboard.feature.home.collections.CollectionsScreen
import com.uragiristereo.mejiboard.feature.home.more.MoreScreen
import com.uragiristereo.mejiboard.lib.navigation_extension.accompanist.AnimatedNavHost
import com.uragiristereo.mejiboard.lib.navigation_extension.accompanist.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeNavGraph(
    onNavigate: (MainRoute) -> Unit,
    onNavigateImage: (Post) -> Unit,
    onCurrentTagsChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedNavHost(
        navController = LocalHomeNavController.current,
        startDestination = HomeRoute.Posts,
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
            disableDeserialization = true,
            content = {
                PostsNavGraph(
                    onNavigateMain = onNavigate,
                    onNavigateImage = onNavigateImage,
                    onCurrentTagsChange = onCurrentTagsChange,
                )
            },
        )

        // Collections
        composable(
            route = HomeRoute.Collections,
            disableDeserialization = true,
            content = {
                CollectionsScreen()
            },
        )

        // More
        composable(
            route = HomeRoute.More,
            disableDeserialization = true,
            content = {
                MoreScreen(
                    onNavigate = onNavigate,
                )
            },
        )
    }
}
