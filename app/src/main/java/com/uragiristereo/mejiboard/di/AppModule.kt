package com.uragiristereo.mejiboard.di

import com.uragiristereo.mejiboard.feature.filters.FiltersViewModel
import com.uragiristereo.mejiboard.feature.home.more.MoreViewModel
import com.uragiristereo.mejiboard.feature.home.posts.PostsViewModel
import com.uragiristereo.mejiboard.feature.image.ImageViewModel
import com.uragiristereo.mejiboard.feature.image.more.MoreBottomSheetViewModel
import com.uragiristereo.mejiboard.feature.search.SearchViewModel
import com.uragiristereo.mejiboard.feature.settings.SettingsViewModel
import com.uragiristereo.mejiboard.saved_searches.SavedSearchesViewModel
import com.uragiristereo.mejiboard.ui.MainViewModel
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
        viewModelOf(::ImageViewModel)
        viewModelOf(::MoreBottomSheetViewModel)
        viewModelOf(::SettingsViewModel)
        viewModelOf(::SavedSearchesViewModel)
    }
}
