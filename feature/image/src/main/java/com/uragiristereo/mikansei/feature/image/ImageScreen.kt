package com.uragiristereo.mikansei.feature.image

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.product.shared.postfavoritevote.PostFavoriteVoteViewModel
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mikansei.core.ui.composable.SetSystemBarsColors
import com.uragiristereo.mikansei.core.ui.extension.areNavigationBarsButtons
import com.uragiristereo.mikansei.core.ui.extension.hideSystemBars
import com.uragiristereo.mikansei.core.ui.extension.showSystemBars
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.navigator.LocalBottomSheetNavigator
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.feature.image.core.LoadingPost
import com.uragiristereo.mikansei.feature.image.image.ImagePost
import com.uragiristereo.mikansei.feature.image.image.UnsupportedPost
import com.uragiristereo.mikansei.feature.image.video.VideoPost
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ImageScreen(
    onNavigateBack: (Boolean) -> Unit,
    onNavigateToMore: (Post) -> Unit,
    onTargetPostChange: (Int) -> Unit,
    viewModel: ViewerViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val window = (context as Activity).window
    val hapticFeedback = LocalHapticFeedback.current
    val lambdaOnDownload = LocalLambdaOnDownload.current
    val bottomSheetNavigator = LocalBottomSheetNavigator.current

    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val targetPostId by viewModel.targetPostId.collectAsStateWithLifecycle()
    val session by viewModel.session.collectAsStateWithLifecycle()

    val targetPost = remember(posts, targetPostId) {
        posts.firstOrNull { it.id == targetPostId }
    }

    val targetPostIndex = when {
        targetPost != null -> posts.indexOf(targetPost)
        else -> null
    }

    LaunchedEffect(key1 = viewModel.areAppBarsVisible) {
        when {
            viewModel.areAppBarsVisible -> window.showSystemBars()
            else -> window.hideSystemBars()
        }
    }

    SetSystemBarsColors(
        statusBarColor = Color.Transparent,
        statusBarDarkIcons = false,
        navigationBarColor = when {
            WindowInsets.navigationBarsIgnoringVisibility.areNavigationBarsButtons() -> Color.Black.copy(
                alpha = 0.7f
            )

            else -> Color.Transparent
        },
        navigationBarDarkIcons = false,
    )
    val saveableStateHolder = rememberSaveableStateHolder()

    if (targetPostIndex != null) {
        val pagerState = rememberPagerState(initialPage = targetPostIndex) {
            posts.size + if (session.canLoadMore) 1 else 0
        }

        val gesturesEnabled =
            pagerState.currentPageOffsetFraction == 0f && viewModel.gesturesEnabled

        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            pageSpacing = 16.dp,
            userScrollEnabled = viewModel.pagerSwipeEnabled,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
        ) { index ->
            if (index != posts.size) {
                val post = posts[index]

                val lambdaOnMoreClick: () -> Unit = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.setAppBarsVisible(true)
                    onNavigateToMore(post)
                }

                val lambdaOnShareClick: () -> Unit = {
                    bottomSheetNavigator.navigate {
                        it.navigate(
                            HomeRoute.Share(
                                post = post,
                                showThumbnail = false,
                            )
                        )
                    }
                }

                LaunchedEffect(
                    key1 = pagerState.isScrollInProgress,
                    key2 = pagerState.settledPage,
                ) {
                    if (!pagerState.isScrollInProgress && pagerState.settledPage == index) {
                        viewModel.setTargetPostId(post.id)
                        onTargetPostChange(post.id)
                    }
                }

                saveableStateHolder.SaveableStateProvider(key = post.id) {
                    koinViewModel<PostFavoriteVoteViewModel>(
                        key = "PostFavoriteVoteViewModel_${post.id}",
                        parameters = {
                            parametersOf(post.id)
                        },
                    )

                    when (post.type) {
                        Post.Type.IMAGE, Post.Type.ANIMATED_GIF -> {
                            ImagePost(
                                areAppBarsVisible = viewModel.areAppBarsVisible,
                                gesturesEnabled = gesturesEnabled,
                                offsetY = viewModel.offsetY::floatValue,
                                onOffsetYChange = viewModel.offsetY.component2(),
                                onNavigateBack = onNavigateBack,
                                onMoreClick = lambdaOnMoreClick,
                                onShareClick = lambdaOnShareClick,
                                onAppBarsVisibleChange = viewModel::setAppBarsVisible,
                                onZoomChange = {
                                    viewModel.currentZoom = it
                                },
                                onPinchGestureChange = {
                                    viewModel.pinchGesture = it
                                },
                                viewModel = koinViewModel(
                                    key = "ImageViewModel_${post.id}",
                                    parameters = {
                                        parametersOf(post)
                                    },
                                ),
                            )
                        }

                        Post.Type.VIDEO, Post.Type.UGOIRA -> {
                            VideoPost(
                                areAppBarsVisible = viewModel.areAppBarsVisible,
                                gesturesEnabled = gesturesEnabled,
                                offsetY = viewModel.offsetY::floatValue,
                                onOffsetYChange = viewModel.offsetY.component2(),
                                onAppBarsVisibleChange = viewModel::setAppBarsVisible,
                                onNavigateBack = onNavigateBack,
                                onMoreClick = lambdaOnMoreClick,
                                onDownloadClick = {
                                    lambdaOnDownload(post)
                                },
                                onShareClick = lambdaOnShareClick,
                                allowPlaying = pagerState.settledPage == index,
                                viewModel = koinViewModel(
                                    key = "VideoViewModel_${post.id}",
                                    parameters = {
                                        parametersOf(post)
                                    },
                                ),
                            )
                        }

                        Post.Type.FLASH, Post.Type.UNSUPPORTED -> {
                            UnsupportedPost(
                                gesturesEnabled = gesturesEnabled,
                                offsetY = viewModel.offsetY::floatValue,
                                onOffsetYChange = viewModel.offsetY.component2(),
                                onNavigateBack = onNavigateBack,
                                onMoreClick = lambdaOnMoreClick,
                                onShareClick = lambdaOnShareClick,
                                viewModel = koinViewModel(
                                    key = post.id.toString(),
                                    parameters = {
                                        parametersOf(post)
                                    },
                                ),
                            )
                        }
                    }
                }
            } else {
                LaunchedEffect(key1 = Unit) {
                    viewModel.getPosts()
                }

                LoadingPost(
                    onNavigateBack = {
                        onNavigateBack(false)
                    },
                )
            }
        }
    }
}
