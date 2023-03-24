package com.uragiristereo.mikansei.core.preferences.model.base

interface PreferenceString : Preference {
    val title: String

    val subtitle: String?
        get() = null
}
