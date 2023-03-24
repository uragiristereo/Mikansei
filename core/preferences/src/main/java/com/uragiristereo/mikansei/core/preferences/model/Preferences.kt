package com.uragiristereo.mikansei.core.preferences.model

import android.os.Build
import androidx.annotation.Keep
import com.uragiristereo.mikansei.core.model.booru.BooruSource
import com.uragiristereo.mikansei.core.model.danbooru.DanbooruHost
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Preferences(
    val theme: ThemePreference = ThemePreference.SYSTEM_DEFAULT,

    @SerialName(value = "black_theme")
    val blackTheme: Boolean = false,

    @SerialName(value = "detail_size")
    val detailSize: DetailSizePreference = DetailSizePreference.COMPRESSED,

    @SerialName(value = "monet_enabled")
    val monetEnabled: Boolean = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> true
        else -> false
    },

    @SerialName(value = "doh_enabled")
    val dohEnabled: Boolean = true,

    val booru: String = BooruSource.Danbooru.key,

    @SerialName(value = "filters_enabled")
    val filtersEnabled: Boolean = true,

    @SerialName(value = "rating_filters")
    val ratingFilters: RatingPreference = RatingPreference.GENERAL_ONLY,

    @SerialName(value = "developer_mode")
    val developerMode: Boolean = false,

    @SerialName(value = "subdomain")
    val subdomain: DanbooruHost = DanbooruHost.Safebooru,

    @SerialName(value = "blacklisted_tags")
    val blacklistedTags: List<String> = listOf(),
)
