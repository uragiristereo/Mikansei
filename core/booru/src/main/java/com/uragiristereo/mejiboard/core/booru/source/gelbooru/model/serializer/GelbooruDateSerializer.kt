package com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object GelbooruDateSerializer : KSerializer<Date> {
    private val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy", Locale.US)

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(serialName = "Date", kind = PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Date {
        val string = decoder.decodeString()

        return dateFormat.parse(string) ?: Date()
    }
}
