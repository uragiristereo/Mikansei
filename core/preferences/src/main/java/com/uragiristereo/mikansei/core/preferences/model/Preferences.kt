package com.uragiristereo.mikansei.core.preferences.model

import android.os.Build
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Preferences(
    @SerialName(value = "theme")
    val theme: ThemePreference = ThemePreference.SYSTEM_DEFAULT,

    @SerialName(value = "black_theme")
    val blackTheme: Boolean = false,

    @SerialName(value = "monet_enabled")
    val monetEnabled: Boolean = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> true
        else -> false
    },

    @SerialName(value = "doh_enabled")
    val dohEnabled: Boolean = true,

    @SerialName(value = "test_mode")
    val testMode: Boolean = false,
)
