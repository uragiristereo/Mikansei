package com.uragiristereo.mejiboard.presentation.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun DragHandle(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                bottom = 4.dp,
            ),
    ) {
        Box(
            modifier = modifier
                .align(Alignment.Center)
                .size(width = 48.dp, height = 4.dp)
                .clip(RoundedCornerShape(percent = 50))
                .background(MaterialTheme.colors.onSurface.copy(alpha = 0.74f)),
        )
    }
}