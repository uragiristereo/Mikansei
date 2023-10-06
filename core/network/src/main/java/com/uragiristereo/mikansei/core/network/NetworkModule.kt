package com.uragiristereo.mikansei.core.network

import com.uragiristereo.mikansei.core.domain.module.network.NetworkRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object NetworkModule {
    operator fun invoke(): Module = module {
        singleOf(::NetworkRepositoryImpl) { bind<NetworkRepository>() }
        singleOf(::DownloadRepositoryImpl) { bind<DownloadRepositoryImpl>() }
    }
}
