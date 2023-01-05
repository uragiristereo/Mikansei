package com.uragiristereo.mejiboard.core.preferences.model

import com.uragiristereo.mejiboard.R

object ThemePreference : BasePreference {
    const val KEY_SYSTEM = "system"
    const val KEY_LIGHT = "light"
    const val KEY_DARK = "dark"

    override val items = listOf(
        PreferenceItem(key = KEY_SYSTEM, titleResId = R.string.settings_theme_item_system_default),
        PreferenceItem(key = KEY_LIGHT, titleResId = R.string.settings_theme_item_light),
        PreferenceItem(key = KEY_DARK, titleResId = R.string.settings_theme_item_dark),
    )
}
