package com.uragiristereo.mejiboard.data.preferences

import android.os.Build
import com.google.gson.annotations.SerializedName
import com.uragiristereo.mejiboard.common.RatingFilter
import com.uragiristereo.mejiboard.data.preferences.entity.DetailSizePreference
import com.uragiristereo.mejiboard.data.preferences.entity.ThemePreference
import com.uragiristereo.mejiboard.data.source.BooruSources
import com.uragiristereo.mejiboard.domain.entity.source.post.Rating

data class Preferences(
    val theme: String = ThemePreference.KEY_SYSTEM,

    @SerializedName(value = "black_theme")
    val blackTheme: Boolean = false,

    @SerializedName(value = "detail_size")
    val detailSize: String = DetailSizePreference.KEY_COMPRESSED,

    @SerializedName(value = "monet_enabled")
    val monetEnabled: Boolean = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> true
        else -> false
    },

    @SerializedName(value = "doh_enabled")
    val dohEnabled: Boolean = true,

    val booru: String = BooruSources.Gelbooru.key,

    @SerializedName(value = "filters_enabled")
    val filtersEnabled: Boolean = true,

    @SerializedName(value = "rating_filters")
    val ratingFilters: List<Rating> = RatingFilter.SAFE,

    @SerializedName(value = "developer_mode")
    val developerMode: Boolean = false,
)
