package com.uragiristereo.mikansei.core.updater.datastore

import androidx.datastore.core.Serializer
import com.uragiristereo.mikansei.core.updater.config.UpdaterConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal object DataStoreUpdaterSerializer : Serializer<UpdaterConfig> {
    private val json = Json {
        encodeDefaults = true
        prettyPrint = true
    }

    override val defaultValue = UpdaterConfig()

    override suspend fun readFrom(input: InputStream): UpdaterConfig {
        return try {
            json.decodeFromString(input.readBytes().decodeToString()) ?: defaultValue
        } catch (t: Throwable) {
            t.printStackTrace()

            defaultValue
        }
    }

    override suspend fun writeTo(t: UpdaterConfig, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(json.encodeToString(t).encodeToByteArray())
        }
    }
}
