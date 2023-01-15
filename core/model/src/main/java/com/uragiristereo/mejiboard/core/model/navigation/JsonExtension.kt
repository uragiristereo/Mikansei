package com.uragiristereo.mejiboard.core.model.navigation

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

//object AnySerializer : SerializationStrategy<Any> {
//    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(serialName = "Any", kind = PrimitiveKind.STRING)
//
//    override fun serialize(encoder: Encoder, value: Any) {
//        encoder.encodeString(value.toString())
//    }
//}

//inline fun <reified T> String.fromJsonBase64Encoded(): T? {
//    return try {
//        val decoded = Base64.decode(this, Base64.DEFAULT)
//            .toString(charset("UTF-8"))
//
//        Json.decodeFromString<T>(string = decoded)
//    } catch (e: IllegalArgumentException) {
//        e.printStackTrace()
//        null
//    }
//}
