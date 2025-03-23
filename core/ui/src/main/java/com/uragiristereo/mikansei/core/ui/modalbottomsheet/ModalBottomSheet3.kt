/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uragiristereo.mikansei.core.ui.modalbottomsheet

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.uragiristereo.mikansei.core.ui.composable.SetSystemBarsColors
import com.uragiristereo.mikansei.core.ui.extension.backgroundElevation
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.SheetValue.Expanded
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.SheetValue.Hidden
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.SheetValue.PartiallyExpanded
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.internal.DraggableAnchors
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.internal.MutablePaddingValues
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.internal.draggableAnchors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

@Composable
fun ModalBottomSheet3(
    modifier: Modifier = Modifier,
    sheetState: SheetState3 = rememberModalBottomSheetState3(),
    useDragHandle: Boolean = true,
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    content: @Composable (contentPadding: PaddingValues) -> Unit,
) {
    if (!sheetState.shouldVisible) {
        return
    }

    val scope = rememberCoroutineScope()
    val animateToDismiss: () -> Unit = {
        if (sheetState.anchoredDraggableState.confirmValueChange(Hidden)) {
            scope.launch { sheetState.hide() }
        }
    }
    val settleToDismiss: (velocity: Float) -> Unit = {
        scope
            .launch { sheetState.settle(it) }
            .invokeOnCompletion { if (!sheetState.isVisible) sheetState.shouldVisible = false }
    }

    val predictiveBackProgress = remember { Animatable(initialValue = 0f) }

    ModalBottomSheetDialog(
        properties = properties,
        onDismissRequest = {
            val dismissToPartiallyExpanded = sheetState.currentValue == Expanded
                    && sheetState.hasPartiallyExpandedState
                    && !sheetState.skipDismissToPartiallyExpanded
            if (dismissToPartiallyExpanded) {
                // Smoothly animate away predictive back transformations since we are not fully
                // dismissing. We don't need to do this in the else below because we want to
                // preserve the predictive back transformations (scale) during the hide animation.
                scope.launch { predictiveBackProgress.animateTo(0f) }
                scope.launch { sheetState.partialExpand() }
            } else { // Is expanded without collapsed state or is collapsed.
                scope.launch { sheetState.hide() }
            }
        },
        predictiveBackProgress = predictiveBackProgress,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .semantics { isTraversalGroup = true },
        ) {
            Scrim(
                color = ScrimColor,
                onDismissRequest = animateToDismiss,
                visible = sheetState.targetValue != Hidden || sheetState.temporarilyHidden,
            )
            ModalBottomSheetContent(
                predictiveBackProgress = predictiveBackProgress,
                scope = scope,
                animateToDismiss = animateToDismiss,
                settleToDismiss = settleToDismiss,
                modifier = modifier,
                sheetState = sheetState,
                useDragHandle = useDragHandle,
                content = content,
            )
        }
    }
    if (sheetState.hasExpandedState) {
        LaunchedEffect(sheetState) { sheetState.showInitial() }
    }
}

@Composable
internal fun BoxScope.ModalBottomSheetContent(
    predictiveBackProgress: Animatable<Float, AnimationVector1D>,
    scope: CoroutineScope,
    animateToDismiss: () -> Unit,
    settleToDismiss: (velocity: Float) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState3 = rememberModalBottomSheetState3(),
    useDragHandle: Boolean = true,
    content: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    val bottomSheetPaneTitle = "Bottom Sheet Pane Title"

    Surface(
        color = MaterialTheme.colors.background.backgroundElevation(2.dp),
        modifier =
        modifier
            .align(Alignment.TopCenter)
            .widthIn(max = MaxWidth)
            .fillMaxWidth()
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal))
                    .union(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            )
            .nestedScroll(
                remember(sheetState) {
                    consumeSwipeWithinBottomSheetBoundsNestedScrollConnection(
                        sheetState = sheetState,
                        orientation = Orientation.Vertical,
                        onFling = settleToDismiss,
                    )
                }
            )
            .draggableAnchors(
                state = sheetState.anchoredDraggableState,
                orientation = Orientation.Vertical,
            ) { sheetSize, constraints ->
                val fullHeight = constraints.maxHeight.toFloat()
                val newAnchors = DraggableAnchors {
                    Hidden at fullHeight
                    if (sheetSize.height > (fullHeight / 2) && !sheetState.skipPartiallyExpanded) {
                        PartiallyExpanded at fullHeight / 2f
                    }
                    if (sheetSize.height != 0) {
                        Expanded at max(0f, fullHeight - sheetSize.height)
                    }
                }
                val newTarget = when (sheetState.anchoredDraggableState.targetValue) {
                    Hidden -> Hidden

                    PartiallyExpanded -> {
                        val hasPartiallyExpandedState = newAnchors.hasAnchorFor(PartiallyExpanded)
                        val newTarget = when {
                            hasPartiallyExpandedState -> PartiallyExpanded
                            newAnchors.hasAnchorFor(Expanded) -> Expanded
                            else -> Hidden
                        }
                        newTarget
                    }

                    Expanded -> when {
                        newAnchors.hasAnchorFor(Expanded) -> Expanded
                        else -> Hidden
                    }
                }
                return@draggableAnchors newAnchors to newTarget
            }
            .draggable(
                state = sheetState.anchoredDraggableState.draggableState,
                orientation = Orientation.Vertical,
                enabled = sheetState.isVisible,
                startDragImmediately = sheetState.anchoredDraggableState.isAnimationRunning,
                onDragStopped = { settleToDismiss(it) },
            )
            .semantics {
                paneTitle = bottomSheetPaneTitle
                traversalIndex = 0f
            }
            .graphicsLayer {
                val sheetOffset = sheetState.anchoredDraggableState.offset
                val sheetHeight = size.height
                if (!sheetOffset.isNaN() && !sheetHeight.isNaN() && sheetHeight != 0f) {
                    val progress = predictiveBackProgress.value
                    scaleX = calculatePredictiveBackScaleX(progress)
                    scaleY = calculatePredictiveBackScaleY(progress)
                    transformOrigin =
                        TransformOrigin(
                            pivotFractionX = 0.5f,
                            pivotFractionY = (sheetOffset + sheetHeight) / sheetHeight,
                        )
                }
            },
        shape = RoundedTopShape,
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    val progress = predictiveBackProgress.value
                    val predictiveBackScaleX = calculatePredictiveBackScaleX(progress)
                    val predictiveBackScaleY = calculatePredictiveBackScaleY(progress)

                    // Preserve the original aspect ratio and alignment of the child content.
                    scaleY = when {
                        predictiveBackScaleY != 0f -> predictiveBackScaleX / predictiveBackScaleY
                        else -> 1f
                    }
                    transformOrigin = PredictiveBackChildTransformOrigin
                }
        ) {
            SetSystemBarsColors(
                color = Color.Transparent,
                darkIcons = MaterialTheme.colors.isLight,
            )

            ModalBottomSheetContentLayout(
                dragHandle = {
                    if (useDragHandle) {
                        DragHandle(
                            sheetState = sheetState,
                            scope = scope,
                            animateToDismiss = animateToDismiss,
                        )
                    }
                },
                content = content,
            )
        }
    }
}

private fun GraphicsLayerScope.calculatePredictiveBackScaleX(progress: Float): Float {
    val width = size.width
    return if (width.isNaN() || width == 0f) {
        1f
    } else {
        1f - lerp(0f, min(PredictiveBackMaxScaleXDistance.toPx(), width), progress) / width
    }
}

private fun GraphicsLayerScope.calculatePredictiveBackScaleY(progress: Float): Float {
    val height = size.height
    return if (height.isNaN() || height == 0f) {
        1f
    } else {
        1f - lerp(0f, min(PredictiveBackMaxScaleYDistance.toPx(), height), progress) / height
    }
}


/**
 * Create and [remember] a [SheetState3] for [ModalBottomSheet3].
 *
 * @param skipPartiallyExpanded Whether the partially expanded state, if the sheet is tall enough,
 *   should be skipped. If true, the sheet will always expand to the [Expanded] state and move to
 *   the [Hidden] state when hiding the sheet, either programmatically or by user interaction.
 * @param confirmValueChange Optional callback invoked to confirm or veto a pending state change.
 */
@Composable
fun rememberModalBottomSheetState3(
    skipPartiallyExpanded: Boolean = false,
    confirmValueChange: (SheetValue) -> Boolean = { true },
    skipDismissToPartiallyExpanded: Boolean = false,
) =
    rememberSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded,
        confirmValueChange = confirmValueChange,
        initialValue = Hidden,
        skipDismissToPartiallyExpanded = skipDismissToPartiallyExpanded,
    )

@Composable
private fun Scrim(color: Color, onDismissRequest: () -> Unit, visible: Boolean) {
    if (color.isSpecified) {
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = TweenSpec(),
            label = "ScrimColorAnimation",
        )
        val closeSheet = "Close sheet"
        val dismissSheet = when {
            visible -> Modifier
                .pointerInput(onDismissRequest) {
                    detectTapGestures { onDismissRequest() }
                }
                .semantics(mergeDescendants = true) {
                    traversalIndex = 1f
                    contentDescription = closeSheet
                    onClick {
                        onDismissRequest()
                        true
                    }
                }

            else -> Modifier
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .then(dismissSheet)
        ) {
            drawRect(color = color, alpha = alpha.coerceIn(0f, 1f))
        }
    }
}

@Composable
private fun DragHandle(
    sheetState: SheetState3,
    scope: CoroutineScope,
    animateToDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val collapseActionLabel = "Collapse action"
    val dismissActionLabel = "Dismiss action"
    val expandActionLabel = "Expand action"

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colors.background.backgroundElevation(2.dp),
                        Color.Transparent,
                    )
                ),
                alpha = 0.7f,
            )
            .semantics(mergeDescendants = true) {
                // Provides semantics to interact with the bottom sheet based on its current value.
                with(sheetState) {
                    dismiss(dismissActionLabel) {
                        animateToDismiss()
                        true
                    }
                    when {
                        currentValue == PartiallyExpanded -> {
                            expand(expandActionLabel) {
                                if (anchoredDraggableState.confirmValueChange(Expanded)) {
                                    scope.launch { sheetState.expand() }
                                }
                                true
                            }
                        }

                        hasPartiallyExpandedState -> {
                            collapse(collapseActionLabel) {
                                if (anchoredDraggableState.confirmValueChange(PartiallyExpanded)) {
                                    scope.launch { partialExpand() }
                                }
                                true
                            }
                        }
                    }
                }
            },
    ) {
        Box(
            modifier = Modifier
                .padding(
                    top = 10.dp,
                    bottom = 8.dp,
                )
                .width(48.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(percent = 50))
                .background(MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)),
        )
    }
}

@Composable
private fun ModalBottomSheetContentLayout(
    dragHandle: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    val innerPadding = remember { MutablePaddingValues() }

    SubcomposeLayout { constraints ->
        val dragHandlePlaceables = subcompose(
            slotId = ModalBottomSheetContentLayoutContent.DragHandle,
            content = dragHandle,
        ).map {
            it.measure(constraints)
        }
        val dragHandleWidth = dragHandlePlaceables.maxOfOrNull { it.width } ?: 0
        val dragHandleHeight = dragHandlePlaceables.maxOfOrNull { it.height } ?: 0

        innerPadding.apply {
            top = dragHandleHeight.toDp()
        }

        val contentPlaceables = subcompose(ModalBottomSheetContentLayoutContent.Content) {
            content(innerPadding)
        }.map {
            it.measure(constraints)
        }
        val contentWidth = contentPlaceables.maxOfOrNull { it.width } ?: 0
        val contentHeight = contentPlaceables.maxOfOrNull { it.height } ?: 0

        val layoutWidth = maxOf(dragHandleWidth, contentWidth)
        val layoutHeight = maxOf(dragHandleHeight, contentHeight)

        layout(layoutWidth, layoutHeight) {
            contentPlaceables.forEach { it.place(0, 0) }
            dragHandlePlaceables.forEach { it.place(0, 0) }
        }
    }
}

private enum class ModalBottomSheetContentLayoutContent { DragHandle, Content }

private val PredictiveBackMaxScaleXDistance = 48.dp
private val PredictiveBackMaxScaleYDistance = 24.dp
private val PredictiveBackChildTransformOrigin = TransformOrigin(0.5f, 0f)
