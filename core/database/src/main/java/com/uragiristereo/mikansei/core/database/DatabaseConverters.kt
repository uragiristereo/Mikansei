package com.uragiristereo.mikansei.core.database

import androidx.room.TypeConverter
import com.uragiristereo.mikansei.core.model.danbooru.Post
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Date

class DatabaseConverters {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    @TypeConverter
    fun timestampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
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
