package com.uragiristereo.mejiboard.feature.image.appbars

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.resources.R
import com.uragiristereo.mejiboard.feature.image.core.ImageLoadingState

@Composable
fun ImageTopAppBar(
    postId: Int,
    visible: Boolean,
    loading: ImageLoadingState,
    originalImageShown: Boolean,
    onNavigateBack: () -> Unit,
    onExpandClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onShareClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        TopAppBar(
            elevation = 0.dp,
            backgroundColor = Color.Transparent,
            contentColor = Color.White,
            title = {
                Text(text = "#$postId")
            },
            navigationIcon = {
                IconButton(
                    onClick = onNavigateBack,
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = null,
                        )
                    },
                )
            },
            actions = {
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        if (!originalImageShown) {
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

                    IconButton(
                        onClick = onMoreClick,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.more_vert),
                                contentDescription = null,
                            )
                        },
                    )
                }
            },
            modifier = modifier
                .background(
                    brush = Brush.verticalGradient(colors = listOf(Color.Black, Color.Transparent)),
                    alpha = 0.5f,
                )
                .statusBarsPadding(),
        )
    }
}
