package com.uragiristereo.mikansei.feature.image.video

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.feature.image.core.verticallyDraggable
import com.uragiristereo.mikansei.feature.image.video.controls.VideoControls
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.abs

@Composable
@androidx.annotation.OptIn(UnstableApi::class)
fun VideoPost(
    areAppBarsVisible: Boolean,
    onAppBarsVisibleChange: (Boolean) -> Unit,
    onNavigateBack: (Boolean) -> Unit,
    onMoreClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VideoViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    val player = viewModel.exoPlayer

    val playerView = remember {
        PlayerView(context).apply {
            this.player = player
            useController = false
            controllerAutoShow = false

            videoSurfaceView?.isHapticFeedbackEnabled = false

            player.prepare()
        }
    }

    DisposableEffect(key1 = lifecycleOwner) {
        var positionUpdaterJob: Job? = null

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                positionUpdaterJob = scope.launch {
                    var lastPosition = 0L

                    while (true) {
                        viewModel.updatePosition(
                            position = player.currentPosition,
                            total = when (player.duration) {
                                C.TIME_UNSET -> 0
                                else -> player.duration
                            },
                        )

                        viewModel.onPlaybackStateChange(
                            isBuffering = lastPosition == viewModel.elapsed && viewModel.isPlaying,
                        )

                        if (viewModel.isPlaying) {
                            lastPosition = viewModel.elapsed
                        }

                        delay(timeMillis = 50L)
                    }
                }
            } else {
                positionUpdaterJob?.cancel()
            }

            when (event) {
                Lifecycle.Event.ON_PAUSE -> player.playWhenReady = false
                Lifecycle.Event.ON_RESUME -> player.playWhenReady = viewModel.isPlaying
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            positionUpdaterJob?.cancel()
        }
    }

    LaunchedEffect(key1 = viewModel.isPlaying) {
        player.playWhenReady = viewModel.isPlaying
    }

    Scaffold(
        scaffoldState = LocalScaffoldState.current,
        topBar = {
            AnimatedVisibility(
                visible = areAppBarsVisible,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .graphicsLayer {
                        translationY = -abs(viewModel.offsetY.value)
                    },
            ) {
                VideoTopAppBar(
                    postId = viewModel.post.id,
                    onNavigateBack = {
                        onNavigateBack(false)
                    },
                    onMoreClick = onMoreClick,
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = areAppBarsVisible,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .graphicsLayer {
                        translationY = abs(viewModel.offsetY.value)
                    },
            ) {
                VideoControls(
                    isPlaying = viewModel.isPlaying,
                    sliderValue = viewModel.sliderValue,
                    elapsed = viewModel.elapsed,
                    total = viewModel.total,
                    sliderValueFmt = viewModel.sliderValueFmt,
                    elapsedFmt = viewModel.elapsedFmt,
                    totalFmt = viewModel.totalFmt,
                    onSeek = viewModel::onSeek,
                    onJump = {
                        player.seekTo(viewModel.sliderValue.toLong())
                        viewModel.onJump()
                    },
                    onPlayingChange = viewModel::onPlayPauseToggle,
                    onDownloadClick = onDownloadClick,
                    onShareClick = onShareClick,
                )
            }
        },
        contentPadding = PaddingValues(0.dp),
        modifier = modifier.fillMaxSize(),
        content = {
            VideoPlayer(
                playerView = playerView,
                isBuffering = viewModel.isBuffering,
                onTap = {
                     onAppBarsVisibleChange(!areAppBarsVisible)
                },
                onDoubleTap = {
                    viewModel.onPlayPauseToggle(!viewModel.isPlaying)
                },
                onLongPress = {
                    onMoreClick()
                },
                modifier = Modifier
                    .graphicsLayer {
                        translationY = viewModel.offsetY.value
                    }
                    .background(Color.Black)
                    .verticallyDraggable(
                        enabled = true,
                        offsetY = viewModel.offsetY,
                        onDragExit = {
                            onAppBarsVisibleChange(true)
                            onNavigateBack(true)
                        },
                    ),
            )
        },
    )
}
