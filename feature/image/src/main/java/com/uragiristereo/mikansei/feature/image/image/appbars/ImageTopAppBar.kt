package com.uragiristereo.mikansei.feature.image.image.appbars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.WindowSize
import com.uragiristereo.mikansei.core.ui.rememberWindowSize

@Composable
internal fun ImageTopAppBar(
    post: Post,
    expandLoadingVisible: Boolean,
    expandButtonVisible: Boolean,
    onNavigateBack: () -> Unit,
    onExpandClick: () -> Unit,
    onDownloadClick: (Post) -> Unit,
    onShareClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowSize = rememberWindowSize()

    TopAppBar(
        elevation = 0.dp,
        backgroundColor = Color.Transparent,
        contentColor = Color.White,
        title = {
            Text(
                text = "#${post.id}",
                color = Color.White,
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = null,
                        tint = Color.White,
                    )
                },
            )
        },
        actions = {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.displayCutoutPadding(),
            ) {
                if (windowSize != WindowSize.COMPACT) {
                    if (expandButtonVisible) {
                        IconButton(
                            onClick = onExpandClick,
                            content = {
                                Icon(
                                    painter = painterResource(id = R.drawable.open_in_full),
                                    contentDescription = null,
                                    tint = Color.White,
                                )
                            },
                        )
                    }

                    if (expandLoadingVisible) {
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
                        onClick = {
                            onDownloadClick(post)
                        },
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.download),
                                contentDescription = null,
                                tint = Color.White,
                            )
                        },
                    )

                    IconButton(
                        onClick = onShareClick,
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.share),
                                contentDescription = null,
                                tint = Color.White,
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
                            tint = Color.White,
                        )
                    },
                )
            }
        },
        modifier = modifier
            .navigationBarsPadding()
            .consumeWindowInsets(WindowInsets.navigationBars)
            .background(
                brush = Brush.verticalGradient(colors = listOf(Color.Black, Color.Transparent)),
                alpha = 0.7f,
            )
            .windowInsetsPadding(
                WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal)
            )
            .statusBarsPadding(),
    )
}
