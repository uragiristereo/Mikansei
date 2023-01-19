package com.uragiristereo.mejiboard.feature.home.posts.core

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.common.ui.WindowSize
import com.uragiristereo.mejiboard.core.common.ui.rememberWindowSize
import com.uragiristereo.mejiboard.core.resources.R

@Composable
fun PostsFab(
    fabVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowSize = rememberWindowSize()

    AnimatedVisibility(
        visible = fabVisible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
        content = {
            FloatingActionButton(
                onClick = onClick,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.expand_less),
                        contentDescription = null,
                    )
                },
                modifier = modifier
                    .navigationBarsPadding()
                    .padding(
                        bottom = when (windowSize) {
                            WindowSize.COMPACT -> 56.dp
                            else -> 0.dp
                        },
                    ),
            )
        },
    )
}
