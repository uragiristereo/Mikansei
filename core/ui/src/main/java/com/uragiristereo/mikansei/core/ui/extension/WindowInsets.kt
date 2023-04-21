package com.uragiristereo.mikansei.core.ui.extension

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun WindowInsets.areNavigationBarsButtons(): Boolean {
    val paddingValues = asPaddingValues()

    return paddingValues.calculateStartPadding(LocalLayoutDirection.current) > 0.dp
            || paddingValues.calculateEndPadding(LocalLayoutDirection.current) > 0.dp
}
