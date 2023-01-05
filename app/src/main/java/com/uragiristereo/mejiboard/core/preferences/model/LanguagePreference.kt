package com.uragiristereo.mejiboard.core.preferences.model

import com.uragiristereo.mejiboard.R

@Suppress("MemberVisibilityCanBePrivate")
object LanguagePreference {
    const val KEY_SYSTEM_DEFAULT = "default"
    const val KEY_EN_US = "en-US"
    const val KEY_ID_ID = "id-ID"
    const val KEY_JA_JP = "ja-JP"

    val items = listOf(
        PreferenceItem(key = KEY_SYSTEM_DEFAULT, titleResId = R.string.settings_theme_item_system_default),
        PreferenceItem(key = KEY_EN_US, titleResId = R.string.settings_language_item_en_US),
        PreferenceItem(key = KEY_ID_ID, titleResId = R.string.settings_language_item_id_ID),
        PreferenceItem(key = KEY_JA_JP, titleResId = R.string.settings_language_item_ja_JP),
    )
}
