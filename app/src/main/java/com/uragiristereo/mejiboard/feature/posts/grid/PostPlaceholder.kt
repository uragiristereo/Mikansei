package com.uragiristereo.mejiboard.feature.posts.grid


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver

@Composable
fun PostPlaceholder(
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
