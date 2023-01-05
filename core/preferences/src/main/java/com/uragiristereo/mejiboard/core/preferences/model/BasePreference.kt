package com.uragiristereo.mejiboard.core.preferences.model

interface BasePreference {
    val items: List<com.uragiristereo.mejiboard.core.model.preferences.PreferenceItem>

    fun getItemByKey(key: String): com.uragiristereo.mejiboard.core.model.preferences.PreferenceItem? {
        return items.firstOrNull { it.key == key }
    }
}
