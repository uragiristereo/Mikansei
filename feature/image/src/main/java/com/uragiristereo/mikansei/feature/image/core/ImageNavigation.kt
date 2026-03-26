package com.uragiristereo.mikansei.feature.image.core

import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.uragiristereo.mikansei.core.product.shared.postfavoritevote.postFavoriteVoteViewModel
import com.uragiristereo.mikansei.core.ui.LocalSharedViewModel
import com.uragiristereo.mikansei.core.ui.animation.translateYFadeIn
import com.uragiristereo.mikansei.core.ui.animation.translateYFadeOut
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.InterceptBackGestureForBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.navigation.PostNavType
import com.uragiristereo.mikansei.feature.image.ImageScreen
import com.uragiristereo.mikansei.feature.image.more.MoreBottomSheet
import com.uragiristereo.mikansei.feature.image.more.tags.TagActionsBottomSheet

fun NavGraphBuilder.imageRoute(
    navController: NavHostController,
) {
    composable<MainRoute.Image>(
        typeMap = PostNavType,
        enterTransition = {
            translateYFadeIn(
                initialOffsetY = { it / 5 },
                durationMillis = 350,
            )
        },
        popExitTransition = {
            val navigatedBackByGesture =
                initialState.savedStateHandle["navigatedBackByGesture"] ?: false

            when {
                navigatedBackByGesture -> fadeOut()
                else -> translateYFadeOut(
                    targetOffsetY = { it / 5 },
                    durationMillis = 350,
                )
            }
        },
    ) { entry ->
        val route = entry.toRoute<MainRoute.Image>()
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val sharedViewModel = LocalSharedViewModel.current

        ImageScreen(
            onNavigateBack = { navigatedBackByGesture ->
                entry.savedStateHandle["navigatedBackByGesture"] = navigatedBackByGesture

                navController.navigateUp()
            },
            onNavigateToMore = { post ->
                bottomSheetNavigator.navigate {
                    it.navigate(
                        MainRoute.More(
                            postId = post.id,
                            sessionId = route.sessionId,
                        )
                    )
                }
            },
            onTargetPostChange = { postId ->
                sharedViewModel.targetPostId = postId
            },
        )
    }
}

fun NavGraphBuilder.imageBottomRoute(navController: NavHostController) {
    composable<MainRoute.More>(PostNavType) { entry ->
        val route = entry.toRoute<MainRoute.More>()
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        InterceptBackGestureForBottomSheetNavigator()

        MoreBottomSheet(
            showExpandButton = false,
            isBottomSheetVisible = bottomSheetNavigator.isVisible,
            onDismiss = bottomSheetNavigator::hideSheet,
            onExpandClick = {},
            onAddToClick = { post ->
                bottomSheetNavigator.navigate {
                    it.navigate(HomeRoute.AddToFavGroup(post))
                }
            },
            onShareClick = { post ->
                bottomSheetNavigator.navigate {
                    it.navigate(HomeRoute.Share(post, showThumbnail = false))
                }
            },
            onTagClick = { tag ->
                bottomSheetNavigator.navigate(popBackStack = false) {
                    it.navigate(MainRoute.TagActions(tag = tag))
                }
            },
            postFavoriteVoteViewModel = route.postFavoriteVoteViewModel(),
        )
    }

    composable<MainRoute.TagActions> { entry ->
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val sharedViewModel = LocalSharedViewModel.current

        val navArgs = entry.toRoute<MainRoute.TagActions>()
        val tag = navArgs.tag

        fun navigateWithTags(tags: String, searchImmediately: Boolean) {
            if (searchImmediately) {
                sharedViewModel.targetPostId = null
                navController.navigate(HomeRoute.Posts(tags)) {
                    popUpTo<HomeRoute.Posts>()
                }
            } else {
                navController.navigate(MainRoute.Search(tags))
            }
        }

        InterceptBackGestureForBottomSheetNavigator()

        TagActionsBottomSheet(
            tag = tag,
            currentSearchTags = sharedViewModel.currentTags,
            onDismiss = bottomSheetNavigator::hideSheet,
            onNavigateBack = bottomSheetNavigator::navigateUp,
            onAddToExisting = { searchImmediately ->
                navigateWithTags(
                    tags = "${sharedViewModel.currentTags}$tag ",
                    searchImmediately = searchImmediately,
                )
            },
            onExcludeToExisting = { searchImmediately ->
                navigateWithTags(
                    tags = "${sharedViewModel.currentTags}-$tag ",
                    searchImmediately = searchImmediately,
                )
            },
            onNewSearch = { searchImmediately ->
                navigateWithTags(
                    tags = "$tag ",
                    searchImmediately = searchImmediately,
                )
            },
        )
    }
}
