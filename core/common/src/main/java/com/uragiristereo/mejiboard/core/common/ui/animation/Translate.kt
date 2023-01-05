package com.uragiristereo.mejiboard.core.common.ui.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

fun translateYFadeIn(
    initialOffsetY: (fullHeight: Int) -> Int = { it / 2 },
    durationMillis: Int = 300,
): EnterTransition = slideInVertically(
    animationSpec = tween(durationMillis),
    initialOffsetY = initialOffsetY,
) + fadeIn(animationSpec = tween(durationMillis))

fun translateYFadeOut(
    targetOffsetY: (fullHeight: Int) -> Int = { it / 2 },
    durationMillis: Int = 300,
): ExitTransition = slideOutVertically(
    animationSpec = tween(durationMillis),
    targetOffsetY = targetOffsetY,
) + fadeOut(animationSpec = tween(durationMillis))

fun translateXFadeIn(
    forward: Boolean,
    durationMillis: Int = 300,
): EnterTransition = slideInHorizontally(
    initialOffsetX = {
        when {
            forward -> it
            else -> -it
        } / 5
    },
) + fadeIn(animationSpec = tween(durationMillis))

fun translateXFadeOut(
    forward: Boolean,
    durationMillis: Int = 300,
): ExitTransition = slideOutHorizontally(
    targetOffsetX = {
        when {
            forward -> -it
            else -> it
        } / 5
    },
) + fadeOut(animationSpec = tween(durationMillis))
