package com.uragiristereo.mikansei.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.uragiristereo.mikansei.core.database.user.UserRepositoryImpl
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

fun databaseModule() = module {
    single { provideDatabase(androidContext()) }

    single { get<MikanseiDatabase>().sessionDao() }
    single { get<MikanseiDatabase>().userDao() }

    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
}

private fun Scope.provideDatabase(context: Context): MikanseiDatabase {
    val preferencesRepository = get<PreferencesRepository>()
    val isTestMode = preferencesRepository.data.value.testMode

    val databaseName = when {
        isTestMode -> "mikansei-database-test.db"
        else -> "mikansei-database.db"
    }

    return Room
        .databaseBuilder(
            context = context,
            klass = MikanseiDatabase::class.java,
            name = databaseName,
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
