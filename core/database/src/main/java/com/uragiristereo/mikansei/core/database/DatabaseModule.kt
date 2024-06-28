package com.uragiristereo.mikansei.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.uragiristereo.mikansei.core.database.post.PostRepositoryImpl
import com.uragiristereo.mikansei.core.database.session.SessionRepositoryImpl
import com.uragiristereo.mikansei.core.database.tag_category.TagCategoryRepositoryImpl
import com.uragiristereo.mikansei.core.database.user.UserRepositoryImpl
import com.uragiristereo.mikansei.core.database.user_delegation.UserDelegationRepositoryImpl
import com.uragiristereo.mikansei.core.domain.module.database.PostRepository
import com.uragiristereo.mikansei.core.domain.module.database.SessionRepository
import com.uragiristereo.mikansei.core.domain.module.database.TagCategoryRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserDelegationRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.Environment
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module

fun databaseModule() = module {
    single { provideDatabase() }

    single { get<MikanseiDatabase>().sessionDao() }
    single { get<MikanseiDatabase>().userDao() }
    single { get<MikanseiDatabase>().searchDelegationDao() }
    single { get<MikanseiDatabase>().postDao() }
    single { get<MikanseiDatabase>().sessionPostDao() }
    single { get<MikanseiDatabase>().tagCategoryDao() }

    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    singleOf(::UserDelegationRepositoryImpl) bind UserDelegationRepository::class
    singleOf(::PostRepositoryImpl) bind PostRepository::class
    singleOf(::SessionRepositoryImpl) bind SessionRepository::class
    singleOf(::TagCategoryRepositoryImpl) bind TagCategoryRepository::class
}

private fun Scope.provideDatabase(): MikanseiDatabase {
    val preferencesRepository = get<PreferencesRepository>()
    val environment = get<Environment>()
    val isTestMode = preferencesRepository.data.value.testMode

    val databaseName = when {
        isTestMode -> "mikansei-database-test.db"
        else -> "mikansei-database.db"
    }

    val showPendingPosts = when {
        environment.safeMode -> "0"
        else -> "1"
    }

    return Room
        .databaseBuilder(
            context = androidContext(),
            klass = MikanseiDatabase::class.java,
            name = databaseName,
        )
        .addCallback(
            object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    db.execSQL("insert into users (id, name, level, is_active, show_pending_posts) values (0, 'Anonymous', 0, 1, $showPendingPosts)")
                }
            })
        .build()
}
