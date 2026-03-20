package com.uragiristereo.mikansei.feature.image.video

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.google.accompanist.insets.ui.Scaffold
import com.uragiristereo.mikansei.core.ui.LocalScaffoldState
import com.uragiristereo.mikansei.feature.image.core.verticallyDraggable
import com.uragiristereo.mikansei.feature.image.video.controls.VideoControls
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun VideoPost(
    player: ExoPlayer?,
    areAppBarsVisible: Boolean,
    gesturesEnabled: Boolean,
    allowPlaying: Boolean,
    offsetY: () -> Float,
    onOffsetYChange: (Float) -> Unit,
    onAppBarsVisibleChange: (Boolean) -> Unit,
    onNavigateBack: (Boolean) -> Unit,
    onMoreClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VideoViewModel,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    val videoMuted by viewModel.videoMuted.collectAsStateWithLifecycle()

    DisposableEffect(key1 = lifecycleOwner, key2 = player) {
        if (player == null) {
            return@DisposableEffect onDispose { }
        }

        if (player.currentMediaItem != viewModel.mediaItem) {
            player.setMediaItem(viewModel.mediaItem, viewModel.elapsed)
        }

        player.repeatMode = Player.REPEAT_MODE_ALL

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
                            isBuffering = lastPosition == viewModel.elapsed && player.playWhenReady,
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
                Lifecycle.Event.ON_RESUME -> player.playWhenReady = viewModel.isPlaying && allowPlaying
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            positionUpdaterJob?.cancel()
        }
    }

    LaunchedEffect(key1 = viewModel.isPlaying, key2 = allowPlaying) {
        player.playWhenReady = viewModel.isPlaying && allowPlaying
    }

    LaunchedEffect(key1 = videoMuted, key2 = player) {
        player?.volume = when {
            videoMuted -> 0f
            else -> 1f
        }
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
                        translationY = -abs(offsetY())
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
                        translationY = abs(offsetY())
                    },
            ) {
                VideoControls(
                    isPlaying = viewModel.isPlaying && allowPlaying,
                    sliderValue = viewModel.sliderValue,
                    elapsed = viewModel.elapsed,
                    total = viewModel.total,
                    sliderValueFmt = viewModel.sliderValueFmt,
                    elapsedFmt = viewModel.elapsedFmt,
                    totalFmt = viewModel.totalFmt,
                    noSound = viewModel.noSound,
                    muted = videoMuted,
                    onSeek = viewModel::onSeek,
                    onJump = {
                        player.seekTo(viewModel.sliderValue.toLong())
                        viewModel.onJump()
                    },
                    onPlayingChange = viewModel::onPlayPauseToggle,
                    onDownloadClick = onDownloadClick,
                    onShareClick = onShareClick,
                    onToggleMuted = viewModel::onToggleVideoMuted,
                )
            }
        },
        contentPadding = PaddingValues(0.dp),
        backgroundColor = Color.Black,
        contentColor = Color.White,
        modifier = modifier.fillMaxSize(),
        content = {
            VideoPlayer(
                player = player,
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
                        translationY = offsetY()
                    }
                    .background(Color.Black)
                    .verticallyDraggable(
                        enabled = gesturesEnabled,
                        offsetY = offsetY,
                        onOffsetYChange = onOffsetYChange,
                        onDragExit = {
                            onAppBarsVisibleChange(true)
                            onNavigateBack(true)
                        },
                    ),
            )
        },
    )
}
