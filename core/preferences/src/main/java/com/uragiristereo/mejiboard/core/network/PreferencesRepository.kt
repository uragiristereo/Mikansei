package com.uragiristereo.mejiboard.core.network

import android.content.Context
import androidx.datastore.core.DataStore
import com.uragiristereo.mejiboard.core.network.model.Preferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val Context.protoDataStore: DataStore<Preferences>

    val dataStore: DataStore<Preferences>

    val flowData: Flow<Preferences>

    val data: Preferences

    suspend fun update(newData: Preferences)

    fun getInitialTheme(): String
}
