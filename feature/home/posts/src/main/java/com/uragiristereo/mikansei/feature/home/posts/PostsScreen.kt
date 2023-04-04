package com.uragiristereo.mikansei.feature.home.posts

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.ShareOption
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.product.component.ProductSetSystemBarsColor
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnDownload
import com.uragiristereo.mikansei.core.ui.LocalLambdaOnShare
import com.uragiristereo.mikansei.core.ui.LocalScrollToTopChannel
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation
import com.uragiristereo.mikansei.core.ui.extension.forEach
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.core.ui.rememberWindowSize
import com.uragiristereo.mikansei.feature.home.posts.core.PostsTopAppBar
import com.uragiristereo.mikansei.feature.home.posts.grid.PostsGrid
import com.uragiristereo.mikansei.feature.home.posts.post_dialog.PostDialog
import com.uragiristereo.mikansei.feature.home.posts.state.PostsContentState
import com.uragiristereo.mikansei.feature.home.posts.state.PostsEmpty
import com.uragiristereo.mikansei.feature.home.posts.state.PostsError
import com.uragiristereo.mikansei.feature.home.posts.state.PostsLoadingState
import com.uragiristereo.mikansei.feature.home.posts.state.PostsProgress
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
internal fun PostsScreen(
    onNavigate: (MainRoute) -> Unit,
    onNavigateImage: (Post) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PostsViewModel = koinViewModel(),
) {
    val density = LocalDensity.current
    val hapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lambdaOnDownload = LocalLambdaOnDownload.current
    val lambdaOnShare = LocalLambdaOnShare.current
    val scrollToTopChannel = LocalScrollToTopChannel.current

    val scope = rememberCoroutineScope()
    val gridState = rememberLazyStaggeredGridState()
    val windowSize = rememberWindowSize()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.loading == PostsLoadingState.FROM_REFRESH,
        onRefresh = viewModel::retryGetPosts,
    )

    val isMoreLoadingVisible by remember {
        derivedStateOf {
            gridState.layoutInfo.visibleItemsInfo
                .filter { it.key.toString() == Constants.KEY_LOAD_MORE_PROGRESS }
                .size == 1
        }
    }

    val areAllItemsVisible by remember {
        derivedStateOf {
            gridState.layoutInfo.visibleItemsInfo.size == viewModel.posts.size
        }
    }

    val isScrollIndexZero by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0
        }
    }

    LaunchedEffect(key1 = Unit) {
        scope.launch {
            viewModel.offsetY.snapTo(targetValue = 0f)
        }
    }

    LaunchedEffect(key1 = viewModel) {
        scrollToTopChannel.forEach {
            scope.launch {
                viewModel.offsetY.animateTo(targetValue = 0f)
            }

            scope.launch {
                gridState.animateScrollToItem(index = 0)
            }
        }
    }

    LaunchedEffect(key1 = viewModel.loading) {
        if (viewModel.loading == PostsLoadingState.DISABLED || viewModel.loading == PostsLoadingState.DISABLED_REFRESHED) {
            viewModel.updatePostsSession()
        }

        if (viewModel.loading == PostsLoadingState.DISABLED_REFRESHED) {
            scope.launch {
                gridState.scrollToItem(index = 0)
                viewModel.offsetY.snapTo(targetValue = 0f)
                viewModel.loading = PostsLoadingState.DISABLED
            }
        }
    }

    LaunchedEffect(key1 = isMoreLoadingVisible) {
        if (isMoreLoadingVisible) {
            viewModel.getPosts(refresh = false)
        }
    }

    LaunchedEffect(key1 = viewModel.jumpToPosition) {
        if (viewModel.jumpToPosition) {
            scope.launch {
                gridState.scrollToItem(
                    index = viewModel.savedState.scrollIndex,
                    scrollOffset = viewModel.savedState.scrollOffset,
                )

                viewModel.jumpToPosition = false
            }
        }
    }

    DisposableEffect(key1 = lifecycleOwner) {
        var job: Job? = null

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    job = scope.launch {
                        while (true) {
                            viewModel.updateSessionPosition(
                                index = gridState.firstVisibleItemIndex,
                                offset = gridState.firstVisibleItemScrollOffset,
                            )

                            delay(timeMillis = 1000L)
                        }
                    }
                }

                else -> job?.cancel()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            job?.cancel()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = gridState.isScrollInProgress) {
        if (!gridState.isScrollInProgress) {
            val topAppBarHeightPx = with(density) { viewModel.topAppBarHeight.toPx() }

            if (viewModel.offsetY.value.roundToInt() != -topAppBarHeightPx.roundToInt() && viewModel.offsetY.value != 0f) {
                val half = topAppBarHeightPx / 2
                val oldOffsetY = viewModel.offsetY.value
                val targetOffsetY = when {
                    kotlin.math.abs(viewModel.offsetY.value) >= half -> -topAppBarHeightPx
                    else -> 0f
                }

                launch {
                    gridState.animateScrollBy(value = oldOffsetY - targetOffsetY)
                }

                launch {
                    viewModel.offsetY.animateTo(targetOffsetY)
                }
            }
        }
    }


    if (viewModel.dialogShown) {
        viewModel.selectedPost?.let { post ->
            PostDialog(
                post = post,
                onDismiss = remember { { viewModel.dialogShown = false } },
                onPostClick = remember {
                    {
                        viewModel.dialogShown = false

                        onNavigateImage(post)
                    }
                },
                onDowloadClick = remember {
                    {
                        viewModel.dialogShown = false

                        lambdaOnDownload(post)
                    }
                },
                onShareClick = remember {
                    {
                        viewModel.dialogShown = false

                        lambdaOnShare(post, ShareOption.COMPRESSED)
                    }
                },
            )
        }
    }

    ProductSetSystemBarsColor(
        navigationBarColor = Color.Transparent,
    )

    Box(
        modifier = modifier
            .statusBarsPadding()
            .nestedScroll(
                connection = remember {
                    object : NestedScrollConnection {
                        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                            val delta = available.y
                            val newOffset = viewModel.offsetY.value + delta

                            scope.launch {
                                if (pullRefreshState.progress == 0f && !areAllItemsVisible) {
                                    viewModel.offsetY.snapTo(
                                        targetValue = newOffset.coerceIn(
                                            minimumValue = with(density) { -viewModel.topAppBarHeight.toPx() },
                                            maximumValue = 0f,
                                        ),
                                    )
                                }
                            }

                            return Offset.Zero
                        }
                    }
                }
            ),
    ) {
        Crossfade(targetState = viewModel.contentState, label = "PostsContent") { target ->
            when (target) {
                PostsContentState.SHOW_POSTS -> {
                    Box(
                        modifier = Modifier.pullRefresh(pullRefreshState),
                    ) {
                        PostsGrid(
                            posts = viewModel.posts,
                            gridState = gridState,
                            canLoadMore = viewModel.canLoadMore,
                            topAppBarHeight = viewModel.topAppBarHeight,
                            onItemClick = onNavigateImage,
                            onItemLongPress = remember {
                                { post ->
                                    viewModel.selectedPost = post
                                    viewModel.dialogShown = true

                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                }
                            },
                        )

                        if (viewModel.loading == PostsLoadingState.FROM_REFRESH || isScrollIndexZero) {
                            PullRefreshIndicator(
                                refreshing = viewModel.loading == PostsLoadingState.FROM_REFRESH,
                                state = pullRefreshState,
                                backgroundColor = MaterialTheme.colors.background.backgroundElevation(),
                                contentColor = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .padding(top = viewModel.topAppBarHeight)
                                    .align(Alignment.TopCenter),
                            )
                        }
                    }
                }

                PostsContentState.SHOW_MAIN_LOADING -> {
                    PostsProgress(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                bottom = when (windowSize) {
                                    WindowSize.COMPACT -> 56.dp + 1.dp
                                    else -> 0.dp
                                },
                            ),
                    )
                }

                PostsContentState.SHOW_EMPTY -> {
                    PostsEmpty(
                        modifier = Modifier.padding(top = 56.dp + 1.dp),
                    )
                }

                PostsContentState.SHOW_ERROR -> {
                    viewModel.errorMessage?.let {
                        PostsError(
                            message = it,
                            onRetryClick = viewModel::retryGetPosts,
                        )
                    }
                }

                PostsContentState.SHOW_NOTHING -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background),
                    )
                }
            }
        }

        PostsTopAppBar(
            searchTags = viewModel.tags,
            onHeightChange = { viewModel.topAppBarHeight = it },
            onSearchClick = { tags ->
                onNavigate(MainRoute.Search(tags))
            },
            onRefreshClick = {
                scope.launch {
                    gridState.animateScrollToItem(index = 0)

                    viewModel.retryGetPosts()
                }
            },
            onExitClick = {
                (context as Activity).finishAffinity()
            },
            modifier = Modifier
                .graphicsLayer {
                    translationY = viewModel.offsetY.value
                },
        )

        AnimatedVisibility(
            visible = viewModel.loading == PostsLoadingState.FROM_LOAD_MORE,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter),
            content = {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.background)
                        .navigationBarsPadding()
                        .padding(
                            bottom = when (windowSize) {
                                WindowSize.COMPACT -> 56.dp + 1.dp
                                else -> 0.dp
                            },
                        ),
                )
            },
        )
    }
}
