package com.uragiristereo.mikansei.core.domain

import com.uragiristereo.mikansei.core.domain.usecase.*
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object DomainModule {
    operator fun invoke(): Module = module {
        factoryOf(::GetPostsUseCase)
        factoryOf(::GetTagsAutoCompleteUseCase)
        factoryOf(::GetTagsUseCase)
        factoryOf(::GetFileSizeUseCase)
        factoryOf(::DownloadPostUseCase)
        factoryOf(::DownloadPostWithNotificationUseCase)
        factoryOf(::ConvertFileSizeUseCase)
    }
}
