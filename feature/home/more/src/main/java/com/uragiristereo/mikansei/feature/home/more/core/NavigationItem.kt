package com.uragiristereo.mikansei.feature.home.more.core

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation

@Composable
internal fun NavigationItem(
    text: String,
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevated: Boolean = true,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                MaterialTheme.colors.background.backgroundElevation(
                    elevation = when {
                        elevated -> 2.dp
                        else -> 1.dp
                    }
                )
            )
            .clickable(onClick = onClick)
            .padding(
                vertical = 12.dp,
                horizontal = 16.dp,
            ),
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            tint = MaterialTheme.colors.primary.copy(alpha = 0.87f),
        )

        Text(text)
    }
}
