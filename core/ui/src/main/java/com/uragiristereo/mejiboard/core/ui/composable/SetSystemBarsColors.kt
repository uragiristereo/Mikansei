package com.uragiristereo.mejiboard.core.ui.composable

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SetSystemBarsColors(
    statusBarColor: Color,
    navigationBarColor: Color,
    statusBarDarkIcons: Boolean,
    navigationBarDarkIcons: Boolean,
) {
    val systemUiController = rememberSystemUiController()

    LaunchedEffect(
        key1 = statusBarColor,
        key2 = navigationBarColor,
    ) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = statusBarDarkIcons,
        )

        systemUiController.setNavigationBarColor(
            color = navigationBarColor,
            darkIcons = navigationBarDarkIcons,
            navigationBarContrastEnforced = false,
        )
    }
}

@Composable
fun SetSystemBarsColors(
    statusBarColor: Color,
    navigationBarColor: Color,
) {
    val isLight = MaterialTheme.colors.isLight

    SetSystemBarsColors(
        statusBarColor = statusBarColor,
        navigationBarColor = navigationBarColor,
        statusBarDarkIcons = isLight,
        navigationBarDarkIcons = isLight,
    )
}

@Composable
fun SetSystemBarsColors(
    color: Color,
    darkIcons: Boolean,
) {
    SetSystemBarsColors(
        statusBarColor = color,
        navigationBarColor = color,
        statusBarDarkIcons = darkIcons,
        navigationBarDarkIcons = darkIcons,
    )
}

@Composable
fun SetSystemBarsColors(
    color: Color,
) {
    SetSystemBarsColors(
        statusBarColor = color,
        navigationBarColor = color,
    )
}
