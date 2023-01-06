package com.uragiristereo.mejiboard.core.preferences.model

import com.uragiristereo.mejiboard.core.model.preferences.PreferenceItem
import com.uragiristereo.mejiboard.core.resources.R

object DetailSizePreference : BasePreference {
    const val KEY_COMPRESSED = "compressed"
    const val KEY_ORIGINAL = "original"

    val COMPRESSED = PreferenceItem(key = KEY_COMPRESSED, titleResId = R.string.settings_image_detail_size_item_compressed)
    val ORIGINAL = PreferenceItem(key = KEY_ORIGINAL, titleResId = R.string.settings_image_detail_size_item_original)

    override val items = listOf(
        COMPRESSED,
        ORIGINAL,
    )
}
