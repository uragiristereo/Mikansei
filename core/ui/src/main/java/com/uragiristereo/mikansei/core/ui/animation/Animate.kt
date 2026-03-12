package com.uragiristereo.mikansei.core.ui.animation

import androidx.compose.animation.core.AnimationResult
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.spring
import androidx.compose.ui.graphics.Color
import androidx.compose.animation.Animatable as AnimatableColor
import androidx.compose.animation.core.Animatable as AnimatableFloat

suspend fun animateFloat(
    initialValue: Float,
    targetValue: Float,
    animationSpec: AnimationSpec<Float> = spring(),
    block: (Float) -> Unit,
): AnimationResult<Float, AnimationVector1D> {
    return AnimatableFloat(initialValue).animateTo(
        targetValue = targetValue,
        animationSpec = animationSpec,
    ) {
        block(value)
    }
}

suspend fun animateColor(
    initialValue: Color,
    targetValue: Color,
    animationSpec: AnimationSpec<Color> = spring(),
    block: (Color) -> Unit,
): AnimationResult<Color, AnimationVector4D> {
    return AnimatableColor(initialValue).animateTo(
        targetValue = targetValue,
        animationSpec = animationSpec,
    ) {
        block(value)
    }
}
