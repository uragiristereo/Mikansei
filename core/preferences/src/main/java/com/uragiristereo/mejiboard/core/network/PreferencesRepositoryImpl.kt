package com.uragiristereo.mejiboard.core.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.uragiristereo.mejiboard.core.network.model.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class PreferencesRepositoryImpl(context: Context) : PreferencesRepository {
    override val Context.protoDataStore by dataStore(
        fileName = "preferences.json",
        serializer = PreferencesSerializer,
    )

    override val dataStore: DataStore<Preferences> = context.protoDataStore
    override val flowData: Flow<Preferences> = context.protoDataStore.data

    override val data: Preferences
        get() = runBlocking { flowData.first() }

    override suspend fun update(newData: Preferences) {
        dataStore.updateData { newData }
    }

    override fun getInitialTheme(): String {
        return data.theme
    }
}
