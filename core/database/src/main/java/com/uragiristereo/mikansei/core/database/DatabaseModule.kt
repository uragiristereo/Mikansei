package com.uragiristereo.mikansei.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.uragiristereo.mikansei.core.database.user.UserRepositoryImpl
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object DatabaseModule {
    operator fun invoke(): Module = module {
        single {
            return@single Room
                .databaseBuilder(
                    context = androidContext(),
                    klass = MikanseiDatabase::class.java,
                    name = "mikansei-database.db",
                )
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            db.execSQL("insert into users (id, name, level, is_active) values (0, 'Anonymous', 0, 1)")
                        }
                    })
                .build()
        }

        single { get<MikanseiDatabase>().sessionDao() }
        single { get<MikanseiDatabase>().userDao() }

        singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    }
}
