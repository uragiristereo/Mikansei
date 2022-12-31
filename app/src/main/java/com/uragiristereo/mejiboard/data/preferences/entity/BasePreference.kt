package com.uragiristereo.mejiboard.data.preferences.entity

interface BasePreference {
    val items: List<PreferenceItem>

    fun getItemByKey(key: String): PreferenceItem? {
        return items.firstOrNull { it.key == key }
    }
}
