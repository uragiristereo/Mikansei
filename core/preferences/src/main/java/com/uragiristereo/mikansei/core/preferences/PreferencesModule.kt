package com.uragiristereo.mikansei.core.preferences

import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object PreferencesModule {
    operator fun invoke(): Module = module {
        singleOf(::PreferencesRepositoryImpl) { bind<PreferencesRepository>() }
    }
}
