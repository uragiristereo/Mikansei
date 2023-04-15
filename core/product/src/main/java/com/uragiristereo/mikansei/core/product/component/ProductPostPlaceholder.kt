package com.uragiristereo.mikansei.core.product.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver

@Composable
fun ProductPostPlaceholder(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colors.primary
                    .copy(alpha = 0.3f)
                    .compositeOver(MaterialTheme.colors.background),
            ),
    )
}
