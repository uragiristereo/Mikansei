package com.uragiristereo.mejiboard.core.model.booru

import android.content.Context
import android.os.Parcelable
import androidx.annotation.StringRes
import com.uragiristereo.mejiboard.core.model.preferences.PreferenceItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class BooruSource(
    val key: String,
    @StringRes val nameResId: Int,
    @StringRes val domainResId: Int,
    val dateFormat: String,
    private val webUrlPattern: String,
) : Parcelable {
    fun baseUrl(context: Context): String {
        val realDomain = context.getString(domainResId)

        return "https://$realDomain"
    }

    fun parseWebUrl(postId: Int): String {
        return this.webUrlPattern
            .replace(
                oldValue = "{postId}",
                newValue = postId.toString(),
            )
    }

    fun toPair(): Pair<String, BooruSource> {
        return this.key to this
    }

    fun toPreferenceItem(): PreferenceItem {
        return PreferenceItem(
            key = key,
            titleResId = nameResId,
            subtitleResId = domainResId,
        )
    }
}
