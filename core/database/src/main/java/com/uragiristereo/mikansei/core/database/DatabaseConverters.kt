package com.uragiristereo.mikansei.core.database

import androidx.room.TypeConverter
import com.uragiristereo.mikansei.core.model.danbooru.Post
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

class DatabaseConverters {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    @TypeConverter
    fun timestampToOffsetDateTime(value: Long?): OffsetDateTime? {
        return value?.let {
            OffsetDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
        }
    }

    @TypeConverter
    fun offsetDateTimeToTimestamp(date: OffsetDateTime?): Long? {
        return date?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun jsonToPost(value: String): Post {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun postToJson(value: Post): String {
        return json.encodeToString(value)
    }
}
