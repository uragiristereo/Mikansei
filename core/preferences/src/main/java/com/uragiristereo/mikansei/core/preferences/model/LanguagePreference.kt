package com.uragiristereo.mikansei.core.preferences.model

import com.uragiristereo.mikansei.core.preferences.model.base.PreferenceStringRes
import com.uragiristereo.mikansei.core.resources.R

enum class LanguagePreference : PreferenceStringRes {
    System_Default {
        override val titleResId = R.string.settings_language_item_default
    },
    en_US {
        override val titleResId = R.string.settings_language_item_en_US
    },
    id_ID {
        override val titleResId = R.string.settings_language_item_id_ID
    },
}
