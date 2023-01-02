package com.uragiristereo.mejiboard.di

import androidx.room.Room
import com.uragiristereo.mejiboard.data.database.DatabaseRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object DatabaseModule {
    operator fun invoke(): Module = module {
        single {
            return@single Room
                .databaseBuilder(
                    /* context = */ androidContext(),
                    /* klass = */ DatabaseRepository::class.java,
                    /* name = */ "mejiboard-database",
                )
                .build()
        }

        single { get<DatabaseRepository>().filtersDao() }
        single { get<DatabaseRepository>().sessionDao() }
    }
}
