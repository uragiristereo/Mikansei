package com.uragiristereo.mejiboard.di

import com.uragiristereo.mejiboard.data.download.DownloadRepository
import com.uragiristereo.mejiboard.data.download.DownloadRepositoryImpl
import com.uragiristereo.mejiboard.data.network.NetworkRepository
import com.uragiristereo.mejiboard.data.network.NetworkRepositoryImpl
import com.uragiristereo.mejiboard.data.source.BoorusRepository
import com.uragiristereo.mejiboard.data.source.BoorusRepositoryImpl
import com.uragiristereo.mejiboard.domain.usecase.DownloadPostUseCase
import com.uragiristereo.mejiboard.domain.usecase.GetFileSizeUseCase
import com.uragiristereo.mejiboard.domain.usecase.GetPostsUseCase
import com.uragiristereo.mejiboard.domain.usecase.GetTagsUseCase
import com.uragiristereo.mejiboard.domain.usecase.SearchTermUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object NetworkModule {
    operator fun invoke(): Module = module {
        singleOf(::NetworkRepositoryImpl) { bind<NetworkRepository>() }
        singleOf(::BoorusRepositoryImpl) { bind<BoorusRepository>() }
        singleOf(::DownloadRepositoryImpl) { bind<DownloadRepository>() }

        factoryOf(::GetPostsUseCase)
        factoryOf(::SearchTermUseCase)
        factoryOf(::GetTagsUseCase)
        factoryOf(::GetFileSizeUseCase)
        factoryOf(::DownloadPostUseCase)
    }
}
