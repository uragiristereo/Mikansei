package com.uragiristereo.mikansei.core.ui.composable

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.uragiristereo.mikansei.core.ui.extension.findActivity

@Composable
fun SetSystemBarsColors(
    statusBarColor: Color,
    navigationBarColor: Color,
    statusBarDarkIcons: Boolean,
    navigationBarDarkIcons: Boolean,
) {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(
        statusBarColor,
        navigationBarColor,
        statusBarDarkIcons,
        navigationBarDarkIcons,
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

        onDispose { }
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
    val isLight = MaterialTheme.colors.isLight

    SetSystemBarsColors(
        statusBarColor = color,
        navigationBarColor = color,
        statusBarDarkIcons = isLight,
        navigationBarDarkIcons = isLight,
    )
}

@Composable
fun SetSystemBarsColorsFromActivity() {
    val context = LocalContext.current
    val activity = remember { context.findActivity() }
    val window = remember { activity?.window }

    if (window != null) {
        val insetsController = remember {
            WindowCompat.getInsetsController(window, window.decorView)
        }

        SetSystemBarsColors(
            statusBarColor = Color(window.statusBarColor),
            navigationBarColor = Color(window.navigationBarColor),
            statusBarDarkIcons = insetsController.isAppearanceLightStatusBars,
            navigationBarDarkIcons = insetsController.isAppearanceLightNavigationBars,
        )
    }
}
