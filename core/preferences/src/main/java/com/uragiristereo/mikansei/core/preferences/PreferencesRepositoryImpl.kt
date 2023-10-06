package com.uragiristereo.mikansei.core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.uragiristereo.mikansei.core.preferences.model.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

class PreferencesRepositoryImpl(
    context: Context,
    coroutineScope: CoroutineScope,
) : PreferencesRepository {
    private val Context.protoDataStore by dataStore(
        fileName = "preferences.json",
        serializer = PreferencesSerializer,
    )

    private val dataStore: DataStore<Preferences> = context.protoDataStore

    override val data = dataStore.data
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = runBlocking {
                dataStore.data.first()
            },
        )

    override suspend fun update(block: suspend (Preferences) -> Preferences) {
        dataStore.updateData(block)
    }
}
