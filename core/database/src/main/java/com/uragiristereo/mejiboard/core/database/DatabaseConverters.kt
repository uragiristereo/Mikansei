package com.uragiristereo.mejiboard.core.database

import androidx.room.TypeConverter
import com.uragiristereo.mejiboard.core.model.booru.post.PostImage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Date

class DatabaseConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun imagePostToString(value: PostImage): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toImagePost(value: String): PostImage? {
        return Json.decodeFromString(value)
    }
}
