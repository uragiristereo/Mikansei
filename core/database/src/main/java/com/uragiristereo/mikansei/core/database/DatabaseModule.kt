package com.uragiristereo.mikansei.core.database

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object DatabaseModule {
    operator fun invoke(): Module = module {
        single {
            return@single Room
                .databaseBuilder(
                    /* context = */ androidContext(),
                    /* klass = */ MikanseiDatabase::class.java,
                    /* name = */ "mikansei-database.db",
                )
                .build()
        }

        single { get<MikanseiDatabase>().sessionDao() }
    }
}
