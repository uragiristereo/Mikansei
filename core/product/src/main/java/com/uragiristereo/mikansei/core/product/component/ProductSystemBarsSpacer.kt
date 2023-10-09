package com.uragiristereo.mikansei.core.product.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

@Composable
fun ProductStatusBarSpacer(modifier: Modifier = Modifier) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsTopHeight(WindowInsets.statusBars)
            .background(
                color = when {
                    MaterialTheme.colors.isLight -> MaterialTheme.colors.primary
                        .copy(alpha = 0.08f)
                        .compositeOver(Color.Black.copy(alpha = 0.02f))
                        .compositeOver(Color.White)

                    else -> MaterialTheme.colors.primary
                        .copy(alpha = 0.08f)
                        .compositeOver(Color.Black.copy(alpha = 0.24f))
                }.compositeOver(MaterialTheme.colors.background),
            ),
    )
}

@Composable
fun ProductStatusBarSpacer(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .background(
                    color = when {
                        MaterialTheme.colors.isLight -> MaterialTheme.colors.primary
                            .copy(alpha = 0.08f)
                            .compositeOver(Color.Black.copy(alpha = 0.02f))
                            .compositeOver(Color.White)

                        else -> MaterialTheme.colors.primary
                            .copy(alpha = 0.08f)
                            .compositeOver(Color.Black.copy(alpha = 0.24f))
                    }.compositeOver(MaterialTheme.colors.background),
                ),
        )

        content(this)
    }
}

@Composable
fun ProductNavigationBarSpacer(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.background.copy(alpha = 0.4f),
) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsBottomHeight(WindowInsets.navigationBars)
            .background(color),
    )
}
