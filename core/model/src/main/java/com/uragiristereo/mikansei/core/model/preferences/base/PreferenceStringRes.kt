package com.uragiristereo.mikansei.core.model.preferences.base

import androidx.annotation.StringRes

interface PreferenceStringRes : Preference {
    @get:StringRes
    val titleResId: Int

    @get:StringRes
    val subtitleResId: Int?
        get() = null
}
