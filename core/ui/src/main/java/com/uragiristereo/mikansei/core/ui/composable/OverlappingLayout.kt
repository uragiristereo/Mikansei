package com.uragiristereo.mikansei.core.ui.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.internal.MutablePaddingValues

@Composable
fun OverlappingLayout(
    contentPadding: PaddingValues,
    overlapContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    val density = LocalDensity.current
    val innerPadding = remember { MutablePaddingValues() }

    SubcomposeLayout(modifier = modifier) { constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val overlapPlaceables = subcompose(
            slotId = 0,
            content = overlapContent,
        ).map {
            it.measure(looseConstraints)
        }
        val overlapWidth = overlapPlaceables.maxOfOrNull { it.width } ?: 0
        val overlapHeight = overlapPlaceables.maxOfOrNull { it.height } ?: 0

        innerPadding.apply {
            top = overlapHeight.toDp()
        }

        val contentPlaceables = subcompose(1) {
            content(innerPadding)
        }.map {
            it.measure(constraints)
        }
        val contentWidth = contentPlaceables.maxOfOrNull { it.width } ?: 0
        val contentHeight = contentPlaceables.maxOfOrNull { it.height } ?: 0

        val layoutWidth = maxOf(overlapWidth, contentWidth)
        val layoutHeight = maxOf(overlapHeight, contentHeight)

        val topHeight = density.run { contentPadding.calculateTopPadding().roundToPx() }

        layout(layoutWidth, layoutHeight) {
            contentPlaceables.forEach { it.place(0, 0) }
            overlapPlaceables.forEach { it.place(0, topHeight) }
        }
    }
}
