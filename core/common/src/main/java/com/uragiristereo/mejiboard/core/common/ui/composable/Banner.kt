package com.uragiristereo.mejiboard.core.common.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.uragiristereo.mejiboard.core.common.ui.WindowSize
import com.uragiristereo.mejiboard.core.common.ui.rememberWindowSize

@Composable
fun Banner(
    icon: Painter,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowSize = rememberWindowSize()

    ConstraintLayout(
        modifier
            .fillMaxSize()
            .padding(
                bottom = when (windowSize) {
                    WindowSize.COMPACT -> 56.dp + 1.dp
                    else -> 0.dp
                },
            )
            .navigationBarsPadding(),
    ) {
        val (iconRef, textRef) = createRefs()

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .constrainAs(iconRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(bottom = 32.dp)
                .size(128.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary),
            content = {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colors.background,
                    modifier = Modifier.size(64.dp),
                )
            },
        )

        Box(
            modifier = Modifier
                .constrainAs(textRef) {
                    top.linkTo(iconRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            content = {
                text()
            },
        )
    }
}
