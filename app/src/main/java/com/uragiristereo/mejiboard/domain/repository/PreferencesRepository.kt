package com.uragiristereo.mejiboard.domain.repository

import android.content.Context
import androidx.datastore.core.DataStore
import com.uragiristereo.mejiboard.data.preferences.Preferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val Context.protoDataStore: DataStore<Preferences>

    val dataStore: DataStore<Preferences>

    val flowData: Flow<Preferences>

    val data: Preferences

    suspend fun update(newData: Preferences)

    fun getInitialTheme(): String
}
