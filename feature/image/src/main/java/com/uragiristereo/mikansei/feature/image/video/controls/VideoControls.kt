package com.uragiristereo.mikansei.feature.image.video.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.feature.image.core.DownloadShareRow

@Composable
internal fun VideoControls(
    isPlaying: Boolean,
    sliderValue: Float,
    elapsed: Long,
    total: Long,
    sliderValueFmt: String,
    elapsedFmt: String,
    totalFmt: String,
    muted: Boolean,
    noSound: Boolean,
    onSeek: (Float) -> Unit,
    onJump: () -> Unit,
    onPlayingChange: (Boolean) -> Unit,
    onDownloadClick: () -> Unit,
    onShareClick: () -> Unit,
    onToggleMuted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sliderInteractionSource = remember { MutableInteractionSource() }
    val isPressed by sliderInteractionSource.collectIsPressedAsState()
    val isDragged by sliderInteractionSource.collectIsDraggedAsState()

    Column(
        modifier = modifier
            .windowInsetsPadding(
                WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal)
            )
            .background(
                brush = Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black)),
                alpha = 0.7f,
            )
            .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal))
            .navigationBarsPadding(),
    ) {
        TimeInfo(
            elapsed = when {
                isPressed || isDragged -> sliderValueFmt
                else -> elapsedFmt
            },
            total = totalFmt,
        )

        ControlSlider(
            value = when {
                isPressed || isDragged -> sliderValue
                else -> elapsed.toFloat()
            },
            max = total.toFloat(),
            onValueChange = onSeek,
            onValueChangeFinished = onJump,
            interactionSource = sliderInteractionSource,
        )

        BottomAppBar(
            elevation = 0.dp,
            backgroundColor = Color.Transparent,
            contentColor = Color.White,
        ) {
            PlayPauseButton(
                isPlaying = isPlaying,
                onPlayClick = {
                    onPlayingChange(true)
                },
                onPauseClick = {
                    onPlayingChange(false)
                },
            )

            SoundButton(
                noSound = noSound,
                muted = muted,
                onToggleMuted = onToggleMuted,
            )

            DownloadShareRow(
                onDownloadClick = onDownloadClick,
                onShareClick = onShareClick,
                modifier = Modifier.weight(1f, fill = true),
            )
        }
    }
}

