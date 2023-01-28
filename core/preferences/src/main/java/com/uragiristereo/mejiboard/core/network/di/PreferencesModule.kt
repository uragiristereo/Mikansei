package com.uragiristereo.mejiboard.core.network.di

import com.uragiristereo.mejiboard.core.network.PreferencesRepository
import com.uragiristereo.mejiboard.core.network.PreferencesRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object PreferencesModule {
    operator fun invoke(): Module = module {
        singleOf(::PreferencesRepositoryImpl) { bind<PreferencesRepository>() }
    }
}
