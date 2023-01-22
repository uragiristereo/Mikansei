package com.uragiristereo.mejiboard.core.database.di

import androidx.room.Room
import com.uragiristereo.mejiboard.core.database.MejiboardDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object DatabaseModule {
    operator fun invoke(): Module = module {
        single {
            return@single Room
                .databaseBuilder(
                    /* context = */ androidContext(),
                    /* klass = */ MejiboardDatabase::class.java,
                    /* name = */ "mejiboard-database.db",
                )
                .build()
        }

        single { get<MejiboardDatabase>().filtersDao() }
        single { get<MejiboardDatabase>().sessionDao() }
        single { get<MejiboardDatabase>().savedSearchesDao() }
    }
}
