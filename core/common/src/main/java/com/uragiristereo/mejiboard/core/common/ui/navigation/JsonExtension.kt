package com.uragiristereo.mejiboard.core.common.ui.navigation

import android.net.Uri
import android.util.Base64
import com.google.gson.Gson

fun Any.toJsonBase64Encoded(gson: Gson = Gson()): String {
    val out = gson.toJson(this)
        .toByteArray()

    return Uri.encode(Base64.encodeToString(out, Base64.DEFAULT))
}

inline fun <reified T> String.fromJsonBase64Encoded(gson: Gson = Gson()): T? {
    return try {
        val decoded = Base64.decode(this, Base64.DEFAULT)
            .toString(charset("UTF-8"))

        gson.fromJson(decoded, T::class.java)
    } catch (e: IllegalArgumentException) {
        null
    }
}
