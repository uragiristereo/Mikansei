package com.uragiristereo.mikansei.feature.image.core

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.ui.animation.animateFloat
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun Modifier.verticallyDraggable(
    enabled: Boolean,
    offsetY: () -> Float,
    onOffsetYChange: (Float) -> Unit,
    onDragExit: () -> Unit,
): Modifier {
    val density = LocalDensity.current
    val maxOffset = density.run { 100.dp.toPx() }

    val draggableState = rememberDraggableState { delta ->
        val deceleratedDragAmount = delta * 0.7f
        val targetValue = offsetY() + deceleratedDragAmount
        onOffsetYChange(
            targetValue.coerceIn(
                minimumValue = -maxOffset,
                maximumValue = maxOffset,
            )
        )
    }

    return draggable(
        state = draggableState,
        orientation = Orientation.Vertical,
        enabled = enabled,
        onDragStopped = {
            delay(timeMillis = 50L)
            if (abs(offsetY()) >= maxOffset * 0.7) {
                onDragExit()
            } else {
                animateFloat(
                    initialValue = offsetY(),
                    targetValue = 0f,
                    block = onOffsetYChange,
                )
            }
        },
    )
}
