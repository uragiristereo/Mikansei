package com.uragiristereo.mejiboard.core.preferences

import androidx.datastore.core.Serializer
import com.google.gson.GsonBuilder
import com.uragiristereo.mejiboard.core.preferences.model.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

object PreferencesSerializer : Serializer<Preferences> {
    override val defaultValue: Preferences
        get() = Preferences()

    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    override suspend fun readFrom(input: InputStream): Preferences {
        return try {
            gson.fromJson(
                /* json = */ input.readBytes().decodeToString(),
                /* classOfT = */ Preferences::class.java,
            ) ?: defaultValue
        } catch (t: Throwable) {
            t.printStackTrace()

            defaultValue
        }
    }

    override suspend fun writeTo(t: Preferences, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(gson.toJson(t).encodeToByteArray())
        }
    }
}
