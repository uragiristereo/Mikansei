package com.uragiristereo.mikansei.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

enum class WindowSize {
    COMPACT,
    MEDIUM,
    EXPANDED,
}

val LocalWindowSizeHorizontal = compositionLocalOf<WindowSize> { error("no LocalWindowSizeHorizontal provided") }
val LocalWindowSizeVertical = compositionLocalOf<WindowSize> { error("no LocalWindowSizeVertical provided") }

@Composable
fun rememberWindowSizeHorizontal(): WindowSize {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    return remember(screenWidthDp) {
        when {
            screenWidthDp < 600.dp -> WindowSize.COMPACT
            screenWidthDp < 840.dp -> WindowSize.MEDIUM
            else -> WindowSize.EXPANDED
        }
    }
}

@Composable
fun rememberWindowSizeVertical(): WindowSize {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp

    return remember(screenHeightDp) {
        when {
            screenHeightDp < 480.dp -> WindowSize.COMPACT
            screenHeightDp < 900.dp -> WindowSize.MEDIUM
            else -> WindowSize.EXPANDED
        }
    }
}
