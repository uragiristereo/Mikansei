package com.uragiristereo.mikansei.core.network.di

import com.uragiristereo.mikansei.core.network.NetworkRepository
import com.uragiristereo.mikansei.core.network.NetworkRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object NetworkModule {
    operator fun invoke(): Module = module {
        singleOf(::NetworkRepositoryImpl) { bind<NetworkRepository>() }
    }
}
