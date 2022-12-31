package com.uragiristereo.mejiboard.presentation.image.more.core

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MoreCloseButton(
    visible: Boolean,
    currentHeight: Dp,
    onHeightChanged: (Dp) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    AnimatedContent(
        targetState = visible,
        modifier = modifier
            .onSizeChanged { size ->
                val newHeight = with(density) { size.height.toDp() }

                if (newHeight != currentHeight) {
                    onHeightChanged(newHeight)
                }
            }
            .padding(horizontal = 8.dp)
            .padding(bottom = 8.dp),
        content = { state ->
            if (state) {
                IconButton(
                    onClick = onClick,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.background.copy(alpha = ContentAlpha.high)),
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.close),
                            contentDescription = null,
                        )
                    },
                )
            }
        },
    )
}