package com.uragiristereo.mejiboard.core.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.uragiristereo.mejiboard.domain.entity.booru.post.PostImage
import java.util.Date

class DatabaseConverters {
    private val gson = Gson()

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
        return gson.toJson(value)
    }

    @TypeConverter
    fun toImagePost(value: String): PostImage? {
        return gson.fromJson(value, PostImage::class.java)
    }
}
