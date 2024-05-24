package com.uragiristereo.mikansei.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uragiristereo.mikansei.core.database.session.SessionDao
import com.uragiristereo.mikansei.core.database.session.SessionRow
import com.uragiristereo.mikansei.core.database.user.UserDao
import com.uragiristereo.mikansei.core.database.user.UserRow
import com.uragiristereo.mikansei.core.database.user_delegation.UserDelegationDao
import com.uragiristereo.mikansei.core.database.user_delegation.UserDelegationRow

@Database(
    entities = [
        SessionRow::class,
        UserRow::class,
        UserDelegationRow::class,
    ],
    version = 1,
)
@TypeConverters(DatabaseConverters::class)
abstract class MikanseiDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao

    abstract fun userDao(): UserDao

    abstract fun searchDelegationDao(): UserDelegationDao
}
