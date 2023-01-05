package com.uragiristereo.mejiboard.core.product.component

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import com.uragiristereo.mejiboard.core.common.ui.composable.SetSystemBarsColors

@Composable
fun ProductSetSystemBarsColor(
    statusBarColor: Color = when {
        MaterialTheme.colors.isLight -> MaterialTheme.colors.primary
            .copy(alpha = 0.08f)
            .compositeOver(Color.Black.copy(alpha = 0.02f))
            .compositeOver(Color.White)

        else -> Color.Black
    },
    navigationBarColor: Color = MaterialTheme.colors.background.copy(alpha = 0.4f),
) {
    SetSystemBarsColors(
        statusBarColor = statusBarColor,
        navigationBarColor = navigationBarColor,
    )
}
