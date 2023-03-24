package com.uragiristereo.mikansei.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uragiristereo.mikansei.core.database.dao.session.SessionDao
import com.uragiristereo.mikansei.core.database.dao.session.SessionRow

@Database(
    entities = [
        SessionRow::class
    ],
    version = 1,
)
@TypeConverters(DatabaseConverters::class)
abstract class MikanseiDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}
