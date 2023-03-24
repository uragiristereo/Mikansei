package com.uragiristereo.mikansei.core.preferences.model.base

import androidx.annotation.StringRes

interface PreferenceStringRes : Preference {
    @get:StringRes
    val titleResId: Int

    @get:StringRes
    val subtitleResId: Int?
        get() = null
}
