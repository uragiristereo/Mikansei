package com.uragiristereo.mejiboard.core.network.model

import android.os.Build
import com.uragiristereo.mejiboard.core.model.RatingFilter
import com.uragiristereo.mejiboard.core.model.booru.BooruSources
import com.uragiristereo.mejiboard.core.model.booru.post.Rating
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Preferences(
    val theme: String = ThemePreference.KEY_SYSTEM,

    @SerialName(value = "black_theme")
    val blackTheme: Boolean = false,

    @SerialName(value = "detail_size")
    val detailSize: String = DetailSizePreference.KEY_COMPRESSED,

    @SerialName(value = "monet_enabled")
    val monetEnabled: Boolean = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> true
        else -> false
    },

    @SerialName(value = "doh_enabled")
    val dohEnabled: Boolean = true,

    val booru: String = BooruSources.Gelbooru.key,

    @SerialName(value = "filters_enabled")
    val filtersEnabled: Boolean = true,

    @SerialName(value = "rating_filters")
    val ratingFilters: List<Rating> = RatingFilter.SAFE,

    @SerialName(value = "developer_mode")
    val developerMode: Boolean = false,
)
