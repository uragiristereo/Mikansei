package com.uragiristereo.mikansei.core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import com.uragiristereo.mikansei.core.preferences.model.Preferences
import com.uragiristereo.mikansei.core.preferences.model.ThemePreference
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val Context.protoDataStore: DataStore<Preferences>

    val dataStore: DataStore<Preferences>

    val flowData: Flow<Preferences>

    val data: Preferences

    suspend fun update(newData: Preferences)

    fun getInitialTheme(): ThemePreference
}
