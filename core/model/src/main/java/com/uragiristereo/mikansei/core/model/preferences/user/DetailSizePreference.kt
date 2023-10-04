package com.uragiristereo.mikansei.core.model.preferences.user

import com.uragiristereo.mikansei.core.model.preferences.base.PreferenceStringRes
import com.uragiristereo.mikansei.core.resources.R
import kotlin.enums.EnumEntries

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

fun EnumEntries<DetailSizePreference>.getEnumFromDanbooru(value: String?): DetailSizePreference {
    return when (value) {
        "large" -> DetailSizePreference.COMPRESSED
        "original" -> DetailSizePreference.ORIGINAL
        else -> DetailSizePreference.COMPRESSED
    }
}
