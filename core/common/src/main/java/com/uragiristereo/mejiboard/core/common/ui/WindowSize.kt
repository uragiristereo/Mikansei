package com.uragiristereo.mejiboard.core.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

enum class WindowSize {
    COMPACT,
    MEDIUM,
    EXPANDED,
}

@Composable
fun rememberWindowSize(): WindowSize {
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
