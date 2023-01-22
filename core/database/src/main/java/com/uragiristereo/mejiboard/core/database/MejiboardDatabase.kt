package com.uragiristereo.mejiboard.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uragiristereo.mejiboard.core.database.dao.filters.FilterTableItem
import com.uragiristereo.mejiboard.core.database.dao.filters.FiltersDao
import com.uragiristereo.mejiboard.core.database.dao.saved_searches.SavedSearchTableItem
import com.uragiristereo.mejiboard.core.database.dao.saved_searches.SavedSearchesDao
import com.uragiristereo.mejiboard.core.database.dao.session.PostSession
import com.uragiristereo.mejiboard.core.database.dao.session.SessionDao

@Database(
    entities = [
        FilterTableItem::class,
        PostSession::class,
        SavedSearchTableItem::class,
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ]
)
@TypeConverters(DatabaseConverters::class)
abstract class MejiboardDatabase : RoomDatabase() {
    abstract fun filtersDao(): FiltersDao

    abstract fun sessionDao(): SessionDao

    abstract fun savedSearchesDao(): SavedSearchesDao
}
