package com.uragiristereo.mikansei.feature.home.posts.core

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.github.uragiristereo.safer.compose.navigation.animation.composable
import com.github.uragiristereo.safer.compose.navigation.core.navigate
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.danbooru.ShareOption
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnShare
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.InterceptBackGestureForBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.home.posts.PostsScreen
import com.uragiristereo.mikansei.feature.home.posts.more.PostMoreContent

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.postsRoute(
    mainNavController: NavHostController,
    onNavigatedBackByGesture: (Boolean) -> Unit,
    onCurrentTagsChange: (String) -> Unit,
) {
    val lambdaOnNavigateImage: (Post) -> Unit = { item ->
        onNavigatedBackByGesture(false)

        mainNavController.navigate(
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
            val bottomSheetNavigator = LocalBottomSheetNavigator.current

            val isRouteFirstEntry = remember {
                // 3 from list of null, MainRoute.Home, HomeRoute.Posts
                mainNavController.currentBackStack.value.size == 3
            }

            LaunchedEffect(key1 = data.tags) {
                onCurrentTagsChange(data.tags)
            }

            InterceptBackGestureForBottomSheetNavigator()

            PostsScreen(
                isRouteFirstEntry = isRouteFirstEntry,
                onNavigateBack = mainNavController::navigateUp,
                onNavigateImage = lambdaOnNavigateImage,
                onNavigateMore = { post ->
                    bottomSheetNavigator.navigate {
                        it.navigate(HomeRoute.PostMore(post))
                    }
                },
            )
        },
    )
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
fun NavGraphBuilder.postsBottomRoute(
    mainNavController: NavHostController,
    onNavigatedBackByGesture: (Boolean) -> Unit,
) {
    composable<HomeRoute.PostMore> {
        val lambdaOnDownload = LocalLambdaOnDownload.current
        val lambdaOnShare = LocalLambdaOnShare.current
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        PostMoreContent(
            onDismiss = bottomSheetNavigator.bottomSheetState::hide,
            onPostClick = { post ->
                bottomSheetNavigator.runHiding {
                    onNavigatedBackByGesture(false)

                    mainNavController.navigate(
                        MainRoute.Image(post)
                    )
                }
            },
            onDownloadClick = lambdaOnDownload,
            onShareClick = { post ->
                lambdaOnShare(post, ShareOption.COMPRESSED)
            },
            onAddToFavoriteGroupClick = { post ->
                bottomSheetNavigator.navigate {
                    it.navigate(HomeRoute.AddToFavGroup(post))
                }
            },
        )
    }
}
