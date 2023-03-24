package com.uragiristereo.mikansei.core.preferences.model

import com.uragiristereo.mikansei.core.preferences.model.base.PreferenceStringRes
import com.uragiristereo.mikansei.core.resources.R

enum class ThemePreference : PreferenceStringRes {
    SYSTEM_DEFAULT {
        override val titleResId = R.string.settings_theme_item_system_default
    },
    LIGHT {
        override val titleResId = R.string.settings_theme_item_light
    },
    DARK {
        override val titleResId = R.string.settings_theme_item_dark
    },
}
