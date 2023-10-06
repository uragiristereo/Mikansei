package com.uragiristereo.mikansei.core.preferences

import com.uragiristereo.mikansei.core.preferences.model.Preferences
import kotlinx.coroutines.flow.StateFlow

interface PreferencesRepository {
    val data: StateFlow<Preferences>

    suspend fun update(block: suspend (Preferences) -> Preferences)
}
