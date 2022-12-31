package com.uragiristereo.mejiboard.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uragiristereo.mejiboard.data.database.filters.FilterTableItem
import com.uragiristereo.mejiboard.data.database.filters.FiltersDao
import com.uragiristereo.mejiboard.data.database.session.PostSession
import com.uragiristereo.mejiboard.data.database.session.SessionDao

@Database(
    entities = [
        FilterTableItem::class,
        PostSession::class,
    ],
    version = 1,
)
@TypeConverters(DatabaseConverters::class)
abstract class DatabaseRepository : RoomDatabase() {
    abstract fun filtersDao(): FiltersDao

    abstract fun sessionDao(): SessionDao
}
