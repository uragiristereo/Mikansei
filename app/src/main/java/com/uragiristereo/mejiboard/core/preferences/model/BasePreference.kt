package com.uragiristereo.mejiboard.core.preferences.model

interface BasePreference {
    val items: List<PreferenceItem>

    fun getItemByKey(key: String): PreferenceItem? {
        return items.firstOrNull { it.key == key }
    }
}
