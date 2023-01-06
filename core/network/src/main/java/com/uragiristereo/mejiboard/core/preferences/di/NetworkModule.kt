package com.uragiristereo.mejiboard.core.preferences.di

import com.uragiristereo.mejiboard.core.preferences.NetworkRepository
import com.uragiristereo.mejiboard.core.preferences.NetworkRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object NetworkModule {
    operator fun invoke(): Module = module {
        singleOf(::NetworkRepositoryImpl) { bind<NetworkRepository>() }
    }
}
