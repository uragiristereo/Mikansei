package com.uragiristereo.mejiboard.feature.settings.preference

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun PreferenceContainer(
    title: String,
    subtitle: AnnotatedString?,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body1,
            color = when {
                !enabled -> MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
                else -> Color.Unspecified
            },
            modifier = Modifier.padding(
                bottom = when {
                    subtitle != null -> 4.dp
                    else -> 0.dp
                },
            ),
        )

        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(
                    alpha = when {
                        !enabled -> ContentAlpha.disabled
                        else -> ContentAlpha.medium
                    },
                )
            )
        }
    }
}
