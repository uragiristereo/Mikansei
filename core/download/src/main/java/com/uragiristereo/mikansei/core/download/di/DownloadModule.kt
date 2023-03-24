package com.uragiristereo.mikansei.core.download.di

import com.uragiristereo.mikansei.core.download.DownloadRepository
import com.uragiristereo.mikansei.core.download.DownloadRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object DownloadModule {
    operator fun invoke(): Module = module {
        singleOf(::DownloadRepositoryImpl) { bind<DownloadRepository>() }
    }
}
