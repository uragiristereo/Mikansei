package com.uragiristereo.mikansei.feature.home.posts.core

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnShare
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.InterceptBackGestureForBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.PostNavType
import com.uragiristereo.mikansei.core.ui.navigation.SavedSearchesRoute
import com.uragiristereo.mikansei.core.ui.navigation.routeOf
import com.uragiristereo.mikansei.feature.home.posts.PostsScreen
import com.uragiristereo.mikansei.feature.home.posts.more.PostMoreContent
import com.uragiristereo.mikansei.feature.home.posts.share.ShareContent

@SuppressLint("RestrictedApi")
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

    composable<HomeRoute.Posts>(
        enterTransition = {
            when (initialState.destination.id) {
                routeOf<HomeRoute.Posts>() -> slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 350),
                )

                else -> null
            }

        },
        exitTransition = {
            when (targetState.destination.id) {
                routeOf<HomeRoute.Posts>() -> slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(durationMillis = 350),
                )

                else -> null
            }
        },
    ) { entry ->
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        val isRouteFirstEntry = remember {
            // 3 from list of null, MainRoute.Home, HomeRoute.Posts
            mainNavController.currentBackStack.value.size == 3
        }

        val args = entry.toRoute<HomeRoute.Posts>()

        LaunchedEffect(key1 = args.tags) {
            onCurrentTagsChange(args.tags)
        }

        PostsScreen(
            isRouteFirstEntry = isRouteFirstEntry,
            onNavigateBack = mainNavController::navigateUp,
            onNavigateImage = lambdaOnNavigateImage,
            onNavigateMore = { post ->
                bottomSheetNavigator.navigate {
                    it.navigate(HomeRoute.PostMore(post))
                }
            },
            onNavigateNewSavedSearch = { tags ->
                mainNavController.navigate(
                    route = SavedSearchesRoute.NewOrEdit(
                        query = tags,
                        savedSearch = null,
                    ),
                )
            }
        )
    }
}

fun NavGraphBuilder.postsBottomRoute(
    mainNavController: NavHostController,
    onNavigatedBackByGesture: (Boolean) -> Unit,
) {
    composable<HomeRoute.PostMore>(PostNavType) {
        val lambdaOnDownload = LocalLambdaOnDownload.current
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        InterceptBackGestureForBottomSheetNavigator()

        PostMoreContent(
            onDismiss = bottomSheetNavigator::hideSheet,
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
                bottomSheetNavigator.navigate {
                    it.navigate(HomeRoute.Share(post))
                }
            },
            onAddToFavoriteGroupClick = { post ->
                bottomSheetNavigator.navigate {
                    it.navigate(HomeRoute.AddToFavGroup(post))
                }
            },
        )
    }

    composable<HomeRoute.Share>(PostNavType) {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val lambdaOnShare = LocalLambdaOnShare.current

        InterceptBackGestureForBottomSheetNavigator()

        ShareContent(
            onDismiss = bottomSheetNavigator::hideSheet,
            onPostClick = { post ->
                bottomSheetNavigator.runHiding {
                    onNavigatedBackByGesture(false)

                    mainNavController.navigate(
                        MainRoute.Image(post)
                    )
                }
            },
            onShareClick = lambdaOnShare,
        )
    }
}
