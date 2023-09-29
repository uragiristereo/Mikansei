package com.uragiristereo.mikansei.feature.image.video.controls

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.with
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.uragiristereo.mikansei.core.resources.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun PlayPauseButton(
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = when {
            isPlaying -> onPauseClick
            else -> onPlayClick
        },
        content = {
            AnimatedContent(
                targetState = isPlaying,
                transitionSpec = {
                    scaleIn() with scaleOut()
                },
                label = "PlayPauseAnimation",
                modifier = modifier,
            ) { state ->
                when {
                    state ->
                        Icon(
                            painter = painterResource(id = R.drawable.pause_fill),
                            contentDescription = null,
                            tint = Color.White,
                        )

                    else ->
                        Icon(
                            painter = painterResource(id = R.drawable.play_arrow_fill),
                            contentDescription = null,
                            tint = Color.White,
                        )
                }
            }
        }
    )
}
