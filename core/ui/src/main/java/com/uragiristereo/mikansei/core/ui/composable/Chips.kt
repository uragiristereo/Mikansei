package com.uragiristereo.mikansei.core.ui.composable

import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@Composable
fun Chips(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    elevation: Dp = 2.dp,
    icon: Painter? = null,
) {
    Chips(
        text = text,
        enabled = enabled,
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
    enabled: Boolean = true,
    elevation: Dp = 2.dp,
    icon: Painter? = null,
    selected: Boolean = false,
) {
    Chips(
        text = text,
        onSelectedChange = onSelectedChange,
        enabled = enabled,
        elevation = elevation,
        selectedIcon = icon,
        unselectedIcon = icon,
        selected = selected,
        modifier = modifier,
    )
}

@Composable
fun Chips(
    text: String,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    elevation: Dp = 2.dp,
    selectedIcon: Painter? = null,
    unselectedIcon: Painter? = null,
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
                        selected -> Color.Transparent
                        else -> MaterialTheme.colors.primary.copy(alpha = ContentAlpha.disabled)
                    },
                ),
                shape = RoundedCornerShape(percent = 50),
            )
            .background(
                color = when {
                    selected -> MaterialTheme.colors.primary
                    else -> MaterialTheme.colors.background.backgroundElevation(elevation)
                }.copy(
                    alpha = when {
                        enabled -> ContentAlpha.high
                        else -> ContentAlpha.disabled
                    },
                ),
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
            .widthIn(48.dp)
    ) {
        val contentColor = when {
            selected -> MaterialTheme.colors.background
            else -> MaterialTheme.colors.primary
        }.copy(
            alpha = when {
                enabled -> ContentAlpha.high
                else -> ContentAlpha.disabled
            },
        )

        val icon = when {
            selected -> selectedIcon
            else -> unselectedIcon
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
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.animateContentSize(),
        )
    }
}
