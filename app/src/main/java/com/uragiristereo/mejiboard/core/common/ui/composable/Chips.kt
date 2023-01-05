package com.uragiristereo.mejiboard.core.common.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uragiristereo.mejiboard.core.common.ui.extension.backgroundElevation

@Composable
fun Chips(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: Dp = 2.dp,
    icon: Painter? = null,
) {
    Chips(
        text = text,
        elevation = elevation,
        onSelectedChange = { onClick() },
        icon = icon,
        modifier = modifier,
    )
}

@Composable
fun Chips(
    text: String,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    elevation: Dp = 2.dp,
    icon: Painter? = null,
    selected: Boolean = false,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(shape = RoundedCornerShape(percent = 50))
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = when {
                        selected -> MaterialTheme.colors.primary
                        else -> MaterialTheme.colors.primary.copy(alpha = ContentAlpha.disabled)
                    },
                ),
                shape = RoundedCornerShape(percent = 50),
            )
            .background(
                color = when {
                    selected -> MaterialTheme.colors.primary
                    else -> MaterialTheme.colors.background.backgroundElevation(elevation)
                }
            )
            .clickable(
                onClick = {
                    onSelectedChange(!selected)
                },
            )
            .padding(
                vertical = 6.dp,
                horizontal = 10.dp,
            )
            .widthIn(56.dp)
    ) {
        val contentColor = when {
            selected -> MaterialTheme.colors.background
            else -> MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high)
        }

        icon?.let {
            Icon(
                painter = it,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.dp),
            )
        }

        Text(
            text = text,
            color = contentColor,
        )
    }
}
