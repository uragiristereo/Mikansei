package com.uragiristereo.mikansei.feature.image.more.core

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MoreSource(
    source: String?,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current

    if (source != null) {
        Text(
            text = source,
            fontSize = 14.sp,
            color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
            modifier = modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        onClick(source)
                    },
                    onLongClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onLongClick(source)
                    },
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 4.dp,
                ),
        )
    }
}
