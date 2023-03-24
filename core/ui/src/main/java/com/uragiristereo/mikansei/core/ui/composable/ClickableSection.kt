package com.uragiristereo.mikansei.core.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ClickableSection(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: AnnotatedString? = null,
    icon: Painter? = null,
    padStart: Boolean = true,
    verticalPadding: Dp = 12.dp,
    horizontalPadding: Dp = 16.dp,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
            )
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding,
            ),
    ) {
        when {
            icon != null -> {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 32.dp)
                        .size(24.dp),
                )
            }

            padStart ->
                Spacer(
                    modifier = Modifier
                        .padding(end = 32.dp)
                        .size(24.dp),
                )
        }

        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
                )
            }
        }
    }
}

@Composable
fun ClickableSection(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String,
    icon: Painter? = null,
    padStart: Boolean = true,
    verticalPadding: Dp = 12.dp,
    horizontalPadding: Dp = 16.dp,
) {
    ClickableSection(
        title = title,
        onClick = onClick,
        modifier = modifier,
        subtitle = AnnotatedString(text = subtitle),
        icon = icon,
        padStart = padStart,
        verticalPadding = verticalPadding,
        horizontalPadding = horizontalPadding,
    )
}
