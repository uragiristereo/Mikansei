package com.uragiristereo.mejiboard.feature.image.appbars

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomAppBar
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.R
import com.uragiristereo.mejiboard.feature.image.core.ImageLoadingState

@Composable
fun ImageBottomAppBar(
    visible: Boolean,
    loading: ImageLoadingState,
    showExpandButton: Boolean,
    onExpandClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black)),
                alpha = 0.5f,
            )
            .navigationBarsPadding(),
    ) {
        BottomAppBar(
            elevation = 0.dp,
            backgroundColor = Color.Transparent,
            contentColor = Color.White,
            content = {
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {
                    if (showExpandButton) {
                        IconButton(
                            onClick = onExpandClick,
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.open_in_full),
                                    contentDescription = null,
                                )
                            },
                        )
                    }

                    if (loading == ImageLoadingState.FROM_EXPAND) {
                        IconButton(
                            onClick = { },
                            enabled = false,
                            content = {
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(24.dp),
                                )
                            },
                        )
                    }

                    IconButton(
                        onClick = onDownloadClick,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.download),
                                contentDescription = null,
                            )
                        },
                    )

                    IconButton(
                        onClick = onShareClick,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.share),
                                contentDescription = null,
                            )
                        },
                    )
                }
            },
        )
    }
}
