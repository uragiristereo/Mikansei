package com.uragiristereo.mikansei

import com.uragiristereo.mikansei.feature.filters.FiltersViewModel
import com.uragiristereo.mikansei.feature.home.more.MoreViewModel
import com.uragiristereo.mikansei.feature.home.posts.PostsViewModel
import com.uragiristereo.mikansei.feature.search.SearchViewModel
import com.uragiristereo.mikansei.feature.settings.SettingsViewModel
import com.uragiristereo.mikansei.saved_searches.SavedSearchesViewModel
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object MikanseiModule {
    operator fun invoke(): Module = module {
        viewModelOf(::MainViewModel)
        viewModelOf(::PostsViewModel)
        viewModelOf(::SearchViewModel)
        viewModelOf(::MoreViewModel)
        viewModelOf(::FiltersViewModel)
        viewModelOf(::SettingsViewModel)
        viewModelOf(::SavedSearchesViewModel)

        single { SupervisorJob() }
        factory { CoroutineScope(context = Dispatchers.Default + get<CompletableJob>()) }
    }
}
