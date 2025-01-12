package com.uragiristereo.mikansei.core.ui.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.KType
import kotlin.reflect.typeOf

inline fun <reified T : Any> navTypeMapOf(): Map<KType, NavType<T?>> {
    val navType = object : NavType<T?>(isNullableAllowed = true) {
        override fun get(bundle: Bundle, key: String): T? {
            val encoded = bundle.getString(key) ?: return null

            return Json.decodeFromString(encoded)
        }

        override fun parseValue(value: String): T {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun put(bundle: Bundle, key: String, value: T?) {
            if (value != null) {
                bundle.putString(key, Json.encodeToString(value))
            }
        }

        override fun serializeAsValue(value: T?): String {
            if (value == null) {
                return "null"
            }

            return Uri.encode(Json.encodeToString(value))
        }
    }

    return mapOf(
        typeOf<T>() to navType,
        typeOf<T?>() to navType,
    )
}
