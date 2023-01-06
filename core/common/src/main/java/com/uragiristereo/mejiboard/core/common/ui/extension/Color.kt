package com.uragiristereo.mejiboard.core.common.ui.extension

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Color.backgroundElevation(
    elevation: Dp = 2.dp,
): Color {
    return MaterialTheme.colors.primary
        .copy(alpha = dpElevationToFloat(elevation))
        .compositeOver(background = this)
}

private fun dpElevationToFloat(elevation: Dp): Float {
    val result = when (elevation) {
        1.dp -> 0.04f
        2.dp -> 0.08f
        else -> elevation.value * 0.08f
    }

    return result.coerceIn(
        minimumValue = 0.04f,
        maximumValue = 1f,
    )
}
