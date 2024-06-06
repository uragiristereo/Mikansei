package com.uragiristereo.mikansei.core.domain

import com.uragiristereo.mikansei.core.domain.usecase.ConvertFileSizeUseCase
import com.uragiristereo.mikansei.core.domain.usecase.DeactivateAccountUseCase
import com.uragiristereo.mikansei.core.domain.usecase.DownloadPostUseCase
import com.uragiristereo.mikansei.core.domain.usecase.DownloadPostWithNotificationUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetFavoriteGroupsUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetFavoritesAndFavoriteGroupsUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetFavoritesUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetPostsUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetTagsAutoCompleteUseCase
import com.uragiristereo.mikansei.core.domain.usecase.GetTagsUseCase
import com.uragiristereo.mikansei.core.domain.usecase.PerformLoginUseCase
import com.uragiristereo.mikansei.core.domain.usecase.SyncUserSettingsUseCase
import com.uragiristereo.mikansei.core.domain.usecase.UpdateUserSettingsUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object DomainModule {
    operator fun invoke(): Module = module {
        factoryOf(::GetPostsUseCase)
        factoryOf(::GetTagsAutoCompleteUseCase)
        factoryOf(::GetTagsUseCase)
        factoryOf(::DownloadPostUseCase)
        factoryOf(::DownloadPostWithNotificationUseCase)
        factoryOf(::ConvertFileSizeUseCase)
        factoryOf(::PerformLoginUseCase)
        factoryOf(::SyncUserSettingsUseCase)
        factoryOf(::UpdateUserSettingsUseCase)
        factoryOf(::GetFavoritesUseCase)
        factoryOf(::GetFavoriteGroupsUseCase)
        factoryOf(::GetFavoritesAndFavoriteGroupsUseCase)
        factoryOf(::DeactivateAccountUseCase)
    }
}
