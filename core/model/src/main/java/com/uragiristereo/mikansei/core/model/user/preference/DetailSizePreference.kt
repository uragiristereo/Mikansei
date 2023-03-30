package com.uragiristereo.mikansei.core.model.user.preference

import com.uragiristereo.mikansei.core.model.preferences.base.PreferenceStringRes
import com.uragiristereo.mikansei.core.resources.R

enum class DetailSizePreference : PreferenceStringRes {
    COMPRESSED {
        override val titleResId = R.string.settings_image_detail_size_item_compressed
    },
    ORIGINAL {
        override val titleResId = R.string.settings_image_detail_size_item_original
    };

    fun getEnumForDanbooru(): String {
        return when (this) {
            COMPRESSED -> "large"
            ORIGINAL -> "original"
        }
    }
}

fun Array<DetailSizePreference>.getEnumFromDanbooru(value: String?): DetailSizePreference {
    return when (value) {
        "large" -> DetailSizePreference.COMPRESSED
        "original" -> DetailSizePreference.ORIGINAL
        else -> DetailSizePreference.COMPRESSED
    }
}
