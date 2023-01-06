package com.uragiristereo.mejiboard.domain.di

import com.uragiristereo.mejiboard.domain.usecase.DownloadPostUseCase
import com.uragiristereo.mejiboard.domain.usecase.DownloadPostWithNotificationUseCase
import com.uragiristereo.mejiboard.domain.usecase.GetFileSizeUseCase
import com.uragiristereo.mejiboard.domain.usecase.GetPostsUseCase
import com.uragiristereo.mejiboard.domain.usecase.GetTagsUseCase
import com.uragiristereo.mejiboard.domain.usecase.SearchTermUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object DomainModule {
    operator fun invoke(): Module = module {
        factoryOf(::GetPostsUseCase)
        factoryOf(::SearchTermUseCase)
        factoryOf(::GetTagsUseCase)
        factoryOf(::GetFileSizeUseCase)
        factoryOf(::DownloadPostUseCase)
        factoryOf(::DownloadPostWithNotificationUseCase)
    }
}
