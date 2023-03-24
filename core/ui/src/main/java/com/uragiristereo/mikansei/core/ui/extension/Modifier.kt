package com.uragiristereo.mikansei.core.ui.extension

import androidx.compose.foundation.layout.*
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
