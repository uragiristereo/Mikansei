package com.uragiristereo.mikansei.feature.home.posts.core

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.dialog
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.uragiristereo.mikansei.core.model.ShareOption
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnShare
import com.uragiristereo.mikansei.core.ui.LocalNavigationRailPadding
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.home.posts.PostsScreen
import com.uragiristereo.mikansei.feature.home.posts.post_dialog.PostDialog

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.postsRoute(
    navController: NavHostController,
    onNavigatedBackByGesture: (Boolean) -> Unit,
    onCurrentTagsChange: (String) -> Unit,
) {
    val lambdaOnNavigateImage: (Post) -> Unit = { item ->
        onNavigatedBackByGesture(false)

        navController.navigate(
            MainRoute.Image(post = item)
        )
    }

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
                onNavigateImage = lambdaOnNavigateImage,
                onNavigateDialog = { post ->
                    navController.navigate(
                        HomeRoute.PostDialog(post)
                    )
                },
                modifier = Modifier
                    .padding(start = LocalNavigationRailPadding.current)
                    .displayCutoutPadding()
                    .windowInsetsPadding(
                        WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal)
                    ),
            )
        },
    )

    dialog(
        route = HomeRoute.PostDialog::class,
        content = { data ->
            val lambdaOnDownload = LocalLambdaOnDownload.current
            val lambdaOnShare = LocalLambdaOnShare.current

            if (data != null) {
                PostDialog(
                    post = data.post,
                    onDismiss = navController::popBackStack,
                    onPostClick = lambdaOnNavigateImage,
                    onDowloadClick = lambdaOnDownload,
                    onShareClick = { post ->
                        lambdaOnShare(post, ShareOption.COMPRESSED)
                    },
                )
            }
        },
    )
}
