package com.uragiristereo.mikansei.core.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize

// From: https://stackoverflow.com/a/73357119/7511276
// Modified a little bit to make it not recomposes everytime with SubcomposeLayout

interface DimensionScope {
    var size: DpSize
}

@Stable
class DimensionScopeImpl(override var size: DpSize = DpSize.Zero) : DimensionScope

@Composable
fun DimensionSubcomposeLayout(
    modifier: Modifier = Modifier,
    content: @Composable DimensionScope.() -> Unit,
) {
    val density = LocalDensity.current

    SubcomposeLayout(modifier = modifier) { constraints ->
        val dimensionScope = DimensionScopeImpl()
        var maxWidth = 0
        var maxHeight = 0

        val placeables = subcompose(slotId = 0) {
            content(dimensionScope)
        }.map {
            it.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }

        placeables.forEach { placeable ->
            if (maxWidth < placeable.width) {
                maxWidth = placeable.width
            }

            if (maxHeight < placeable.height) {
                maxHeight = placeable.height
            }
        }

        dimensionScope.size = density.run {
            DpSize(
                width = maxWidth.toDp(),
                height = maxHeight.toDp(),
            )
        }

        layout(width = maxWidth, height = maxHeight) {
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = 0)
            }
        }
    }
}
