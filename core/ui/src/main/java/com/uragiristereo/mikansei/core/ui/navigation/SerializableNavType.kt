package com.uragiristereo.mikansei.core.ui.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@OptIn(InternalSerializationApi::class)
abstract class SerializableNavType<T : Any>(private val klass: KClass<T>) : NavType<T>(true) {
    override fun get(bundle: Bundle, key: String): T? {
        return Json.decodeFromString(klass.serializer(), requireNotNull(bundle.getString(key)))
    }

    override fun parseValue(value: String): T {
        return Json.decodeFromString(klass.serializer(), Uri.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, Json.encodeToString(klass.serializer(), value))
    }

    override fun serializeAsValue(value: T): String {
        return Uri.encode(Json.encodeToString(klass.serializer(), value))
    }
}

inline fun <reified T : Any> navTypeMapOf(): Map<KType, NavType<T>> {
    val navType = object : SerializableNavType<T>(T::class) {}

    return mapOf(
        typeOf<T>() to navType,
        typeOf<T?>() to navType,
    )
}
