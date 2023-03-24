package com.uragiristereo.mikansei.core.preferences.model

import com.uragiristereo.mikansei.core.preferences.model.base.PreferenceStringRes
import com.uragiristereo.mikansei.core.resources.R

enum class DetailSizePreference : PreferenceStringRes {
    COMPRESSED {
        override val titleResId = R.string.settings_image_detail_size_item_compressed
    },
    ORIGINAL {
        override val titleResId = R.string.settings_image_detail_size_item_original
    },
}
