package com.uragiristereo.mikansei.core.ui.extension

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
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        hide(WindowInsetsCompat.Type.systemBars())
    }
}
