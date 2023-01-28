package com.uragiristereo.mejiboard.core.network

import androidx.datastore.core.Serializer
import com.uragiristereo.mejiboard.core.network.model.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object PreferencesSerializer : Serializer<Preferences> {
    private val json = Json {
        encodeDefaults = true
        prettyPrint = true
    }

    override val defaultValue: Preferences
        get() = Preferences()

    override suspend fun readFrom(input: InputStream): Preferences {
        return try {
            json.decodeFromString(input.readBytes().decodeToString()) ?: defaultValue
        } catch (t: Throwable) {
            t.printStackTrace()

            defaultValue
        }
    }

    override suspend fun writeTo(t: Preferences, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(json.encodeToString(t).encodeToByteArray())
        }
    }
}
