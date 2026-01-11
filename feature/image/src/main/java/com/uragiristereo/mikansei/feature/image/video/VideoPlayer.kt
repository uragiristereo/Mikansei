package com.uragiristereo.mikansei.feature.image.video

import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBarsIgnoringVisibility
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.feature.image.databinding.LayoutVideoPlayerBinding

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun VideoPlayer(
    player: ExoPlayer,
    postType: Post.Type,
    isBuffering: Boolean,
    onTap: () -> Unit,
    onDoubleTap: () -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        AndroidViewBinding(LayoutVideoPlayerBinding::inflate) {
            val playerView = when (postType) {
                Post.Type.UGOIRA -> playerViewGl
                else -> playerViewNative
            }
            playerView.visibility = View.VISIBLE
            playerView.player = player
            playerView.videoSurfaceView?.isHapticFeedbackEnabled = false
            player.prepare()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(
                    WindowInsets.systemBarsIgnoringVisibility.only(WindowInsetsSides.Bottom)
                )
                .combinedClickable(
                    interactionSource = null,
                    indication = null,
                    onClick = onTap,
                    onDoubleClick = onDoubleTap,
                    onLongClick = onLongPress,
                ),
        )

        AnimatedVisibility(
            visible = isBuffering,
            enter = fadeIn(),
            exit = fadeOut(),
            content = {
                CircularProgressIndicator()
            },
        )
    }
}
