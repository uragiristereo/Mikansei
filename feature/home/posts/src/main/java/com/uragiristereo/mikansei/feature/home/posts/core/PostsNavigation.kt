package com.uragiristereo.mikansei.feature.home.posts.core

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.dialog
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.danbooru.ShareOption
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnShare
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.home.posts.PostsScreen
import com.uragiristereo.mikansei.feature.home.posts.post_dialog.PostDialog
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
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
            val currentBackStack by navController.currentBackStack.collectAsState()

            val isRouteFirstEntry by remember {
                derivedStateOf {
                    // 3 from list of null, MainRoute.Home, HomeRoute.Posts
                    currentBackStack.size == 3
                }
            }

            LaunchedEffect(key1 = data.tags) {
                onCurrentTagsChange(data.tags)
            }

            PostsScreen(
                isRouteFirstEntry = isRouteFirstEntry,
                onNavigateBack = navController::navigateUp,
                onNavigateImage = lambdaOnNavigateImage,
                onNavigateDialog = { post ->
                    navController.navigate(
                        HomeRoute.PostDialog(post)
                    )
                },
            )
        },
    )

    dialog<HomeRoute.PostDialog> { data ->
        val lambdaOnDownload = LocalLambdaOnDownload.current
        val lambdaOnShare = LocalLambdaOnShare.current

        val scope = rememberCoroutineScope()

        if (data != null) {
            PostDialog(
                post = data.post,
                onDismiss = navController::popBackStack,
                onPostClick = lambdaOnNavigateImage,
                onDownloadClick = lambdaOnDownload,
                onShareClick = { post ->
                    lambdaOnShare(post, ShareOption.COMPRESSED)
                },
                onAddToFavoriteGroupClick = { post ->
                    scope.launch(SupervisorJob()) {
                        delay(timeMillis = 300L)

                        navController.navigate(HomeRoute.AddToFavGroup(post))
                    }
                },
            )
        }
    }
}
