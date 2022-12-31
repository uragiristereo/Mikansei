package com.uragiristereo.mejiboard.presentation.image.more.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MoreInfo(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = title,
            color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
        )

        Text(
            text = subtitle,
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colors.background.copy(alpha = 0.4f))
                .padding(
                    vertical = 4.dp,
                    horizontal = 8.dp,
                ),
        )
    }
}