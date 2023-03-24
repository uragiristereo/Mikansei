package com.uragiristereo.mikansei

import com.uragiristereo.mikansei.feature.filters.FiltersViewModel
import com.uragiristereo.mikansei.feature.home.more.MoreViewModel
import com.uragiristereo.mikansei.feature.home.posts.PostsViewModel
import com.uragiristereo.mikansei.feature.search.SearchViewModel
import com.uragiristereo.mikansei.feature.settings.SettingsViewModel
import com.uragiristereo.mikansei.saved_searches.SavedSearchesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object MikanseiAppModule {
    operator fun invoke(): Module = module {
        viewModelOf(::MainViewModel)
        viewModelOf(::PostsViewModel)
        viewModelOf(::SearchViewModel)
        viewModelOf(::MoreViewModel)
        viewModelOf(::FiltersViewModel)
        viewModelOf(::SettingsViewModel)
        viewModelOf(::SavedSearchesViewModel)
    }
}
