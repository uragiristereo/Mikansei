package com.uragiristereo.mejiboard.app.di

import com.uragiristereo.mejiboard.app.MainViewModel
import com.uragiristereo.mejiboard.app.home.HomeViewModel
import com.uragiristereo.mejiboard.feature.filters.FiltersViewModel
import com.uragiristereo.mejiboard.feature.image.ImageViewModel
import com.uragiristereo.mejiboard.feature.image.more.MoreBottomSheetViewModel
import com.uragiristereo.mejiboard.feature.more.MoreViewModel
import com.uragiristereo.mejiboard.feature.posts.PostsViewModel
import com.uragiristereo.mejiboard.feature.search.SearchViewModel
import com.uragiristereo.mejiboard.feature.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModule {
    operator fun invoke(): Module = module {
        viewModelOf(::MainViewModel)
        viewModelOf(::PostsViewModel)
        viewModelOf(::SearchViewModel)
        viewModelOf(::MoreViewModel)
        viewModelOf(::FiltersViewModel)
        viewModelOf(::HomeViewModel)
        viewModelOf(::ImageViewModel)
        viewModelOf(::MoreBottomSheetViewModel)
        viewModelOf(::SettingsViewModel)
    }
}
