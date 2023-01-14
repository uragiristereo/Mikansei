package com.uragiristereo.mejiboard.core.common.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.DpSize

// From: https://stackoverflow.com/a/73357119/7511276

interface DimensionScope {
    var size: DpSize
}

class DimensionScopeImpl(override var size: DpSize = DpSize.Zero) : DimensionScope

@Composable
fun DimensionLayout(
    modifier: Modifier = Modifier,
    content: @Composable DimensionScope.() -> Unit,
) {
    val density = LocalDensity.current
    val dimensionScope = remember { DimensionScopeImpl() }

    Layout(
        modifier = modifier,
        // 🔥 since we invoke it here it will have Size.Zero
        // on Composition then will have size value below
        content = { dimensionScope.content() }
    ) { measurables: List<Measurable>, constraints: Constraints ->

        val placeables = measurables.map { measurable: Measurable ->
            measurable.measure(constraints)
        }

        val maxWidth = placeables.maxOf { it.width }
        val maxHeight = placeables.maxOf { it.height }

        dimensionScope.size = density.run {
            DpSize(
                width = maxWidth.toDp(),
                height = maxHeight.toDp()
            )
        }

        layout(maxWidth, maxHeight) {
            placeables.forEach { placeable: Placeable ->
                placeable.placeRelative(0, 0)
            }
        }
    }
}
