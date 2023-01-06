package com.uragiristereo.mejiboard.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uragiristereo.mejiboard.core.database.dao.filters.FilterTableItem
import com.uragiristereo.mejiboard.core.database.dao.session.PostSession
import com.uragiristereo.mejiboard.core.database.dao.session.SessionDao
import com.uragiristereo.mejiboard.core.database.database.dao.filters.FiltersDao

@Database(
    entities = [
        FilterTableItem::class,
        PostSession::class,
    ],
    version = 1,
)
@TypeConverters(DatabaseConverters::class)
abstract class MejiboardDatabase : RoomDatabase() {
    abstract fun filtersDao(): FiltersDao

    abstract fun sessionDao(): SessionDao
}
