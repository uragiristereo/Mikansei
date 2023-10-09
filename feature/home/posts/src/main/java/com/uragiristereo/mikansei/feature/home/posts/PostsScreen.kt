package com.uragiristereo.mikansei.feature.home.posts

import android.app.Activity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.product.component.ProductStatusBarSpacer
import com.uragiristereo.mikansei.core.ui.LocalMainScaffoldPadding
import com.uragiristereo.mikansei.core.ui.LocalScrollToTopChannel
import com.uragiristereo.mikansei.core.ui.LocalWindowSizeHorizontal
import com.uragiristereo.mikansei.core.ui.LocalWindowSizeVertical
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.composable.DimensionSubcomposeLayout
import com.uragiristereo.mikansei.core.ui.composable.SetSystemBarsColors
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation
import com.uragiristereo.mikansei.core.ui.extension.forEach
import com.uragiristereo.mikansei.core.ui.extension.horizontalOnly
import com.uragiristereo.mikansei.core.ui.extension.verticalOnly
import com.uragiristereo.mikansei.feature.home.posts.core.PostsTopAppBar
import com.uragiristereo.mikansei.feature.home.posts.grid.PostsGrid
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
    isRouteFirstEntry: Boolean,
    onNavigateBack: () -> Unit,
    onNavigateImage: (Post) -> Unit,
    onNavigateDialog: (Post) -> Unit,
    viewModel: PostsViewModel = koinViewModel(),
) {
    val density = LocalDensity.current
    val hapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scrollToTopChannel = LocalScrollToTopChannel.current
    val windowSizeVertical = LocalWindowSizeVertical.current
    val windowSizeHorizontal = LocalWindowSizeHorizontal.current

    val scope = rememberCoroutineScope()
    val gridState = rememberLazyStaggeredGridState()
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

    val shouldEnableTopAppBarScroll by remember {
        derivedStateOf {
            when {
                windowSizeVertical == WindowSize.COMPACT && windowSizeHorizontal == WindowSize.MEDIUM -> true
                windowSizeVertical == WindowSize.MEDIUM && windowSizeHorizontal == WindowSize.COMPACT -> true
                else -> false
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        scope.launch {
            viewModel.offsetY.snapTo(targetValue = 0f)
        }
    }

    LaunchedEffect(key1 = shouldEnableTopAppBarScroll) {
        scope.launch {
            viewModel.offsetY.animateTo(targetValue = 0f)
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

    SetSystemBarsColors(Color.Transparent)

    Scaffold(
        topBar = {
            DimensionSubcomposeLayout(
                modifier = Modifier
                    .statusBarsPadding()
                    .graphicsLayer {
                        translationY = viewModel.offsetY.value
                    },
            ) {
                LaunchedEffect(key1 = size.height) {
                    viewModel.topAppBarHeight = size.height
                }

                PostsTopAppBar(
                    searchTags = viewModel.tags,
                    isRouteFirstEntry = isRouteFirstEntry,
                    onNavigateBack = onNavigateBack,
                    onRefreshClick = {
                        scope.launch {
                            gridState.animateScrollToItem(index = 0)

                            viewModel.retryGetPosts()
                        }
                    },
                    onExitClick = {
                        (context as Activity).finishAffinity()
                    },
                )
            }

            ProductStatusBarSpacer()
        },
        contentPadding = LocalMainScaffoldPadding.current.verticalOnly,
        modifier = Modifier
            .padding(LocalMainScaffoldPadding.current.horizontalOnly)
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.End))
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.End)),
    ) { innerPadding ->
        LaunchedEffect(key1 = gridState.isScrollInProgress) {
            if (!gridState.isScrollInProgress) {
                val topAppBarHeightPx = density.run { viewModel.topAppBarHeight.toPx() }

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

        Box(
            modifier = Modifier
                .nestedScroll(
                    connection = remember(shouldEnableTopAppBarScroll) {
                        object : NestedScrollConnection {
                            override fun onPreScroll(
                                available: Offset,
                                source: NestedScrollSource
                            ): Offset {
                                val delta = available.y
                                val newOffset = viewModel.offsetY.value + delta

                                scope.launch {
                                    if (pullRefreshState.progress == 0f && !areAllItemsVisible && shouldEnableTopAppBarScroll) {
                                        viewModel.offsetY.snapTo(
                                            targetValue = newOffset.coerceIn(
                                                minimumValue = density.run { -viewModel.topAppBarHeight.toPx() },
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
            Crossfade(
                targetState = viewModel.contentState,
                label = "PostsContent",
            ) { target ->
                when (target) {
                    PostsContentState.SHOW_POSTS -> {
                        Box(
                            modifier = Modifier.pullRefresh(pullRefreshState),
                        ) {
                            PostsGrid(
                                posts = viewModel.posts,
                                gridState = gridState,
                                canLoadMore = viewModel.canLoadMore,
                                contentPadding = innerPadding,
                                onItemClick = onNavigateImage,
                                onItemLongPress = { post ->
                                    onNavigateDialog(post)
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                },
                            )

                            if (viewModel.loading == PostsLoadingState.FROM_REFRESH || isScrollIndexZero) {
                                PullRefreshIndicator(
                                    refreshing = viewModel.loading == PostsLoadingState.FROM_REFRESH,
                                    state = pullRefreshState,
                                    backgroundColor = MaterialTheme.colors.background.backgroundElevation(),
                                    contentColor = MaterialTheme.colors.primary,
                                    modifier = Modifier
                                        .padding(innerPadding)
                                        .align(Alignment.TopCenter)
                                        .graphicsLayer {
                                            translationY = viewModel.offsetY.value
                                        },
                                )
                            }
                        }
                    }

                    PostsContentState.SHOW_MAIN_LOADING -> {
                        PostsProgress(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                        )
                    }

                    PostsContentState.SHOW_EMPTY -> {
                        PostsEmpty(
                            modifier = Modifier.padding(innerPadding),
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
        }
    }
}
