package com.uragiristereo.mikansei.core.model.preferences

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class PreferenceItem(
    val key: String,
    @StringRes val titleResId: Int,
    @StringRes val subtitleResId: Int? = null,
) : Parcelable
