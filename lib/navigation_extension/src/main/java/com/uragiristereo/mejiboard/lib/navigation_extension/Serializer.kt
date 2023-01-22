package com.uragiristereo.mejiboard.lib.navigation_extension

import android.net.Uri
import android.util.Base64
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import timber.log.Timber

object Serializer {
    private val navRoutes = mutableMapOf<String, PolymorphicModuleBuilder<NavRoute>.() -> Unit>()
    private var module: SerializersModule? = null

    val json: Json
        get() = Json {
            ignoreUnknownKeys = true
            module?.let { serializersModule = it }
        }

    fun addPolymorphicType(
        name: String,
        builder: PolymorphicModuleBuilder<NavRoute>.() -> Unit,
    ) {
        navRoutes[name] = (builder)

        updateModule()
    }

    private fun updateModule() {
        module = SerializersModule {
            polymorphic(NavRoute::class) {
                navRoutes.forEach { (_, builder) ->
                    builder()
                }
            }
        }
    }

    inline fun <reified T> encode(value: T): String {
        val data = json.encodeToString(value)

        return Uri.encode(Base64.encodeToString(data.toByteArray(), Base64.DEFAULT))
    }

    inline fun <reified T> decode(value: String): T? {
        return try {
            val decoded = Base64
                .decode(value, Base64.DEFAULT)
                .toString(charset("UTF-8"))

            json.decodeFromString(decoded)
        } catch (e: IllegalArgumentException) {
            Timber.w(e.message)

            null
        }
    }
}
