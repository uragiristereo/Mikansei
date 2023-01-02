package com.uragiristereo.mejiboard.di

import com.uragiristereo.mejiboard.data.preferences.PreferencesRepositoryImpl
import com.uragiristereo.mejiboard.domain.repository.PreferencesRepository
import com.uragiristereo.mejiboard.presentation.MainViewModel
import com.uragiristereo.mejiboard.presentation.filters.FiltersViewModel
import com.uragiristereo.mejiboard.presentation.home.HomeViewModel
import com.uragiristereo.mejiboard.presentation.home.route.more.MoreViewModel
import com.uragiristereo.mejiboard.presentation.home.route.posts.PostsViewModel
import com.uragiristereo.mejiboard.presentation.image.ImageViewModel
import com.uragiristereo.mejiboard.presentation.image.more.MoreBottomSheetViewModel
import com.uragiristereo.mejiboard.presentation.search.SearchViewModel
import com.uragiristereo.mejiboard.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object AppModule {
    val module = module {
        singleOf(::PreferencesRepositoryImpl) { bind<PreferencesRepository>() }

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
