package com.uragiristereo.mikansei.core.preferences.model

import com.uragiristereo.mikansei.core.model.Rating
import com.uragiristereo.mikansei.core.preferences.model.base.PreferenceStringRes
import com.uragiristereo.mikansei.core.resources.R

enum class RatingPreference : PreferenceStringRes {
    GENERAL_ONLY {
        override val titleResId = R.string.settings_booru_listing_mode_item_general
    },
    SAFE {
        override val titleResId = R.string.settings_booru_listing_mode_item_safe
    },
    NO_EXPLICIT {
        override val titleResId = R.string.settings_booru_listing_mode_item_no_explicit
    },
    UNFILTERED {
        override val titleResId = R.string.settings_booru_listing_mode_item_unfiltered
    };

    fun getFilteredRatings(): List<Rating> {
        return when (this) {
            GENERAL_ONLY -> listOf(Rating.SENSITIVE, Rating.QUESTIONABLE, Rating.EXPLICIT)
            SAFE -> listOf(Rating.QUESTIONABLE, Rating.EXPLICIT)
            NO_EXPLICIT -> listOf(Rating.EXPLICIT)
            UNFILTERED -> emptyList()
        }
    }
}
