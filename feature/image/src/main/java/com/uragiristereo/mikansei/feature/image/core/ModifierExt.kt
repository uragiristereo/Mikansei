package com.uragiristereo.mikansei.feature.image.core

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

fun Modifier.verticallyDraggable(
    enabled: Boolean,
    offsetY: Animatable<Float, AnimationVector1D>,
    onDragExit: suspend () -> Unit,
) = composed {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val maxOffset = remember { density.run { 100.dp.toPx() } }

    this.pointerInput(enabled) {
        if (enabled) {
            detectDragGestures(
                onDragEnd = {
                    scope.launch {
                        delay(timeMillis = 50L)

                        if (abs(offsetY.value) >= maxOffset * 0.7) {
                            onDragExit()
                        } else {
                            offsetY.animateTo(targetValue = 0f)
                        }
                    }
                },
                onDrag = { change, dragAmount ->
                    change.consume()

                    val deceleratedDragAmount = dragAmount.y * 0.7f

                    if (abs(offsetY.value + deceleratedDragAmount) <= maxOffset) {
                        scope.launch {
                            offsetY.snapTo(offsetY.value + deceleratedDragAmount)
                        }
                    }
                },
            )
        }
    }
}

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
                Animatable(offsetY()).animateTo(targetValue = 0f) {
                    onOffsetYChange(value)
                }
            }
        },
    )
}
