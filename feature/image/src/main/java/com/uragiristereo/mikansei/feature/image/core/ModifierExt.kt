package com.uragiristereo.mikansei.feature.image.core

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.gestures.detectDragGestures
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
    key1: Any = Unit,
    offsetY: Animatable<Float, AnimationVector1D>,
    onDragExit: suspend () -> Unit,
) = composed {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val maxOffset = remember { with(density) { 100.dp.toPx() } }

    this.pointerInput(key1) {
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
