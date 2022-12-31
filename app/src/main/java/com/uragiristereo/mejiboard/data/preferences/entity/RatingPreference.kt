package com.uragiristereo.mejiboard.data.preferences.entity

import com.uragiristereo.mejiboard.R

object RatingPreference : BasePreference {
    const val KEY_GENERAL_ONLY = "general_only"
    const val KEY_SAFE = "safe"
    const val KEY_NO_EXPLICIT = "no_explicit"
    const val KEY_UNFILTERED = "unfiltered"

    override val items = listOf(
        PreferenceItem(key = KEY_GENERAL_ONLY, titleResId = R.string.settings_booru_listing_mode_item_general),
        PreferenceItem(key = KEY_SAFE, titleResId = R.string.settings_booru_listing_mode_item_safe),
        PreferenceItem(key = KEY_NO_EXPLICIT, titleResId = R.string.settings_booru_listing_mode_item_no_explicit),
        PreferenceItem(key = KEY_UNFILTERED, titleResId = R.string.settings_booru_listing_mode_item_unfiltered),
    )

    val itemsSafe = listOf(
        PreferenceItem(key = KEY_GENERAL_ONLY, titleResId = R.string.settings_booru_listing_mode_item_general),
        PreferenceItem(key = KEY_SAFE, titleResId = R.string.settings_booru_listing_mode_item_safe),
    )
}
