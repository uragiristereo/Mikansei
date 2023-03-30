package com.uragiristereo.mikansei.core.model.preferences.base

interface PreferenceString : Preference {
    val title: String

    val subtitle: String?
        get() = null
}
