package com.uragiristereo.mikansei.core.updater.datastore

import android.content.Context
import androidx.datastore.dataStore
import com.uragiristereo.mikansei.core.updater.config.UpdaterConfig
import com.uragiristereo.mikansei.core.updater.config.UpdaterConfigRepository
import kotlinx.coroutines.flow.first

internal class DataStoreUpdaterConfigRepository(context: Context) : UpdaterConfigRepository {
    private val configFileName = "mikansei-updater.json"

    private val Context.protoDataStore by dataStore(
        fileName = configFileName,
        serializer = DataStoreUpdaterSerializer,
    )

    private val configDataStore = context.protoDataStore

    override suspend fun readConfig(): UpdaterConfig {
        return configDataStore.data.first()
    }

    override suspend fun writeConfig(block: (UpdaterConfig) -> UpdaterConfig) {
        configDataStore.updateData(block)
    }
}
