package com.uragiristereo.mejiboard.core.common.ui.extension

import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun Window.showSystemBars() {
    WindowCompat.getInsetsController(this, decorView)
        .show(WindowInsetsCompat.Type.systemBars())
}

fun Window.hideSystemBars() {
    WindowCompat.getInsetsController(this, decorView).apply {
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
        hide(WindowInsetsCompat.Type.systemBars())
    }
}
