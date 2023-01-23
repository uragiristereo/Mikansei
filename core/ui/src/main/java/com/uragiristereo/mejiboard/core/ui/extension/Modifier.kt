package com.uragiristereo.mejiboard.core.ui.extension

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.defaultPaddings(): Modifier = composed {
    this
        .statusBarsPadding()
        .windowInsetsPadding(
            insets = WindowInsets.navigationBars.only(sides = WindowInsetsSides.Horizontal),
        )
        .displayCutoutPadding()
}
