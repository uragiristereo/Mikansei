package com.uragiristereo.mikansei.core.ui.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

fun holdIn(durationMillis: Int = 300): EnterTransition = fadeIn(
    animationSpec = tween(durationMillis),
    initialAlpha = 1f,
)

fun holdOut(durationMillis: Int = 300): ExitTransition = fadeOut(
    animationSpec = tween(durationMillis),
    // Refer: https://issuetracker.google.com/issues/192993290
    // targetAlpha = 1f,
    targetAlpha = 0.999f,
)
