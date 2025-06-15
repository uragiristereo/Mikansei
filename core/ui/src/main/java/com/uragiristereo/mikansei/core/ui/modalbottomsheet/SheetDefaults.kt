/*
 * Copyright 2023 The Android Open Source Project
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

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.SheetValue.Expanded
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.SheetValue.Hidden
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.SheetValue.PartiallyExpanded
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.internal.AnchoredDraggableState
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.internal.animateTo
import com.uragiristereo.mikansei.core.ui.modalbottomsheet.internal.snapTo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

/**
 * State of a sheet composable, such as [ModalBottomSheet3]
 *
 * Contains states relating to its swipe position as well as animations between state values.
 *
 * @param skipPartiallyExpanded Whether the partially expanded state, if the sheet is large enough,
 *   should be skipped. If true, the sheet will always expand to the [Expanded] state and move to
 *   the [Hidden] state if available when hiding the sheet, either programmatically or by user
 *   interaction.
 * @param initialValue The initial value of the state.
 * @param density The density that this state can use to convert values to and from dp.
 * @param confirmValueChange Optional callback invoked to confirm or veto a pending state change.
 * @param skipHiddenState Whether the hidden state should be skipped. If true, the sheet will always
 *   expand to the [Expanded] state and move to the [PartiallyExpanded] if available, either
 *   programmatically or by user interaction.
 * @param skipDismissToPartiallyExpanded Whether the [PartiallyExpanded] state should be skipped,
 *   If true, the sheet will always dismiss to the [Hidden] state, regardless if the [PartiallyExpanded]
 *   state is available.
 */
@Stable
class SheetState3(
    internal val skipPartiallyExpanded: Boolean,
    density: Density,
    internal val initialValue: SheetValue = Hidden,
    confirmValueChange: (SheetValue) -> Boolean = { true },
    internal val skipHiddenState: Boolean = false,
    internal val skipDismissToPartiallyExpanded: Boolean = false,
) {
    init {
        if (skipPartiallyExpanded) {
            require(initialValue != PartiallyExpanded) {
                "The initial value must not be set to PartiallyExpanded if skipPartiallyExpanded " +
                        "is set to true."
            }
        }
        if (skipHiddenState) {
            require(initialValue != Hidden) {
                "The initial value must not be set to Hidden if skipHiddenState is set to true."
            }
        }
    }

    internal var requestedTarget: SheetValue? = null

    internal var shouldVisible by mutableStateOf(false)

    internal var temporarilyHidden by mutableStateOf(false)

    /**
     * The current value of the state.
     *
     * If no swipe or animation is in progress, this corresponds to the state the bottom sheet is
     * currently in. If a swipe or an animation is in progress, this corresponds the state the sheet
     * was in before the swipe or animation started.
     */
    val currentValue: SheetValue
        get() = anchoredDraggableState.currentValue

    /**
     * The target value of the bottom sheet state.
     *
     * If a swipe is in progress, this is the value that the sheet would animate to if the swipe
     * finishes. If an animation is running, this is the target value of that animation. Finally, if
     * no swipe or animation is in progress, this is the same as the [currentValue].
     */
    val targetValue: SheetValue
        get() = anchoredDraggableState.targetValue

    /** Whether the modal bottom sheet is visible. */
    val isVisible: Boolean
        get() = anchoredDraggableState.currentValue != Hidden

    /** Whether an animation is currently in progress. */
    val isAnimationRunning: Boolean
        get() = anchoredDraggableState.isAnimationRunning

    /**
     * Require the current offset (in pixels) of the bottom sheet.
     *
     * The offset will be initialized during the first measurement phase of the provided sheet
     * content.
     *
     * These are the phases: Composition { -> Effects } -> Layout { Measurement -> Placement } ->
     * Drawing
     *
     * During the first composition, an [IllegalStateException] is thrown. In subsequent
     * compositions, the offset will be derived from the anchors of the previous pass. Always prefer
     * accessing the offset from a LaunchedEffect as it will be scheduled to be executed the next
     * frame, after layout.
     *
     * @throws IllegalStateException If the offset has not been initialized yet
     */
    fun requireOffset(): Float = anchoredDraggableState.requireOffset()

    /** Whether the sheet has an expanded state defined. */
    val hasExpandedState: Boolean
        get() = anchoredDraggableState.anchors.hasAnchorFor(Expanded)

    /** Whether the modal bottom sheet has a partially expanded state defined. */
    val hasPartiallyExpandedState: Boolean
        get() = anchoredDraggableState.anchors.hasAnchorFor(PartiallyExpanded)

    /**
     * Fully expand the bottom sheet with animation and suspend until it is fully expanded or
     * animation has been cancelled.
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    suspend fun expand() {
        if (shouldVisible) {
            animateTo(Expanded)
        }

        requestedTarget = Expanded
        shouldVisible = true
        temporarilyHidden = false
    }

    /**
     * Animate the bottom sheet and suspend until it is partially expanded or animation has been
     * cancelled.
     *
     * @throws [CancellationException] if the animation is interrupted
     * @throws [IllegalStateException] if [skipPartiallyExpanded] is set to true
     */
    suspend fun partialExpand() {
        check(!skipPartiallyExpanded) {
            "Attempted to animate to partial expanded when skipPartiallyExpanded was enabled. Set" +
                    " skipPartiallyExpanded to false to use this function."
        }

        if (shouldVisible) {
            animateTo(PartiallyExpanded)
        }

        requestedTarget = PartiallyExpanded
        shouldVisible = true
        temporarilyHidden = false
    }

    /**
     * Expand the bottom sheet with animation and suspend until it is [PartiallyExpanded] if defined
     * else [Expanded].
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    suspend fun show() {
        val targetValue = when {
            hasPartiallyExpandedState -> PartiallyExpanded
            else -> Expanded
        }

        if (shouldVisible) {
            animateTo(targetValue)
        }

        requestedTarget = targetValue
        shouldVisible = true
    }

    internal suspend fun showInitial() {
        when {
            requestedTarget == Expanded -> expand()
            requestedTarget == PartiallyExpanded -> show()
            initialValue == Expanded -> expand()
            initialValue == PartiallyExpanded -> show()
        }
    }

    /**
     * Hide the bottom sheet with animation and suspend until it is fully hidden or animation has
     * been cancelled.
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    suspend fun hide() {
        check(!skipHiddenState) {
            "Attempted to animate to hidden when skipHiddenState was enabled. Set skipHiddenState" +
                    " to false to use this function."
        }

        try {
            animateTo(Hidden)
        } catch (t: Throwable) {
            throw t
        } finally {
            if (!isVisible) {
                shouldVisible = false
            }
        }
    }

    suspend fun hideTemporarily() {
        check(!skipHiddenState) {
            "Attempted to animate to hidden when skipHiddenState was enabled. Set skipHiddenState" +
                    " to false to use this function."
        }

        temporarilyHidden = true
        animateTo(Hidden)
    }

    /**
     * Animate to a [targetValue]. If the [targetValue] is not in the set of anchors, the
     * [currentValue] will be updated to the [targetValue] without updating the offset.
     *
     * @param targetValue The target value of the animation
     * @throws CancellationException if the interaction interrupted by another interaction like a
     *   gesture interaction or another programmatic interaction like a [animateTo] or [snapTo]
     *   call.
     */
    internal suspend fun animateTo(
        targetValue: SheetValue,
        velocity: Float = anchoredDraggableState.lastVelocity,
    ) {
        anchoredDraggableState.animateTo(targetValue, velocity)
    }

    /**
     * Snap to a [targetValue] without any animation.
     *
     * @param targetValue The target value of the animation
     * @throws CancellationException if the interaction interrupted by another interaction like a
     *   gesture interaction or another programmatic interaction like a [animateTo] or [snapTo]
     *   call.
     */
    internal suspend fun snapTo(targetValue: SheetValue) {
        anchoredDraggableState.snapTo(targetValue)
    }

    /**
     * Find the closest anchor taking into account the velocity and settle at it with an animation.
     */
    internal suspend fun settle(velocity: Float) {
        anchoredDraggableState.settle(velocity)
    }

    internal var anchoredDraggableState = AnchoredDraggableState(
        initialValue = initialValue,
        animationSpec = BottomSheetAnimationSpec,
        confirmValueChange = confirmValueChange,
        positionalThreshold = { with(density) { 56.dp.toPx() } },
        velocityThreshold = { with(density) { 125.dp.toPx() } },
        confirmAnimate = { progress ->
            val toExpanded = currentValue == Expanded && targetValue == Expanded
            val toPartiallyExpanded = currentValue == PartiallyExpanded
                    && targetValue == PartiallyExpanded
            val result = ((!toExpanded && !toPartiallyExpanded) || (progress > 0f && progress < 1f))
            val isTemporarilyHiddenForTransition = currentValue == Hidden
                    && targetValue == Expanded
                    && progress == 0f
                    && temporarilyHidden
            if (isTemporarilyHiddenForTransition) {
                delay(timeMillis = 50L)
            }
            result
        },
    )

    internal val offset: Float
        get() = anchoredDraggableState.offset

    companion object {
        /** The default [Saver] implementation for [SheetState3]. */
        fun Saver(
            skipPartiallyExpanded: Boolean,
            confirmValueChange: (SheetValue) -> Boolean,
            density: Density,
            skipHiddenState: Boolean,
            skipDismissToPartiallyExpanded: Boolean,
        ) =
            Saver<SheetState3, Array<Any>>(
                save = { arrayOf(it.currentValue, it.shouldVisible) },
                restore = { savedValue ->
                    val initialValue = savedValue[0] as SheetValue
                    val shouldVisible = savedValue[1] as Boolean

                    SheetState3(
                        skipPartiallyExpanded = skipPartiallyExpanded,
                        density = density,
                        initialValue = initialValue,
                        confirmValueChange = confirmValueChange,
                        skipHiddenState = skipHiddenState,
                        skipDismissToPartiallyExpanded = skipDismissToPartiallyExpanded,
                    ).apply {
                        this.shouldVisible = shouldVisible
                    }
                }
            )
    }
}

/** Possible values of [SheetState3]. */
enum class SheetValue {
    /** The sheet is not visible. */
    Hidden,

    /** The sheet is visible at full height. */
    Expanded,

    /** The sheet is partially visible. */
    PartiallyExpanded,
}

internal fun consumeSwipeWithinBottomSheetBoundsNestedScrollConnection(
    sheetState: SheetState3,
    orientation: Orientation,
    onFling: (velocity: Float) -> Unit,
): NestedScrollConnection =
    object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val delta = available.toFloat()
            return if (delta < 0 && source == NestedScrollSource.UserInput) {
                sheetState.anchoredDraggableState.dispatchRawDelta(delta).toOffset()
            } else {
                Offset.Zero
            }
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            return if (source == NestedScrollSource.UserInput) {
                sheetState.anchoredDraggableState.dispatchRawDelta(available.toFloat()).toOffset()
            } else {
                Offset.Zero
            }
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            val toFling = available.toFloat()
            val currentOffset = sheetState.requireOffset()
            val minAnchor = sheetState.anchoredDraggableState.anchors.minAnchor()
            return if (toFling < 0 && currentOffset > minAnchor) {
                onFling(toFling)
                // since we go to the anchor with tween settling, consume all for the best UX
                available
            } else {
                Velocity.Zero
            }
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            onFling(available.toFloat())
            return available
        }

        private fun Float.toOffset(): Offset =
            Offset(
                x = if (orientation == Orientation.Horizontal) this else 0f,
                y = if (orientation == Orientation.Vertical) this else 0f
            )

        @JvmName("velocityToFloat")
        private fun Velocity.toFloat() = if (orientation == Orientation.Horizontal) x else y

        @JvmName("offsetToFloat")
        private fun Offset.toFloat(): Float = if (orientation == Orientation.Horizontal) x else y
    }

@Composable
internal fun rememberSheetState(
    skipPartiallyExpanded: Boolean = false,
    confirmValueChange: (SheetValue) -> Boolean = { true },
    initialValue: SheetValue = Hidden,
    skipHiddenState: Boolean = false,
    skipDismissToPartiallyExpanded: Boolean = false,
): SheetState3 {
    val density = LocalDensity.current
    return rememberSaveable(
        skipPartiallyExpanded,
        confirmValueChange,
        skipHiddenState,
        skipDismissToPartiallyExpanded,
        saver =
        SheetState3.Saver(
            skipPartiallyExpanded = skipPartiallyExpanded,
            confirmValueChange = confirmValueChange,
            density = density,
            skipHiddenState = skipHiddenState,
            skipDismissToPartiallyExpanded = skipDismissToPartiallyExpanded,
        )
    ) {
        SheetState3(
            skipPartiallyExpanded,
            density,
            initialValue,
            confirmValueChange,
            skipHiddenState,
            skipDismissToPartiallyExpanded,
        )
    }
}

/** The default animation spec used by [SheetState3]. */
private val BottomSheetAnimationSpec: AnimationSpec<Float> = spring()
