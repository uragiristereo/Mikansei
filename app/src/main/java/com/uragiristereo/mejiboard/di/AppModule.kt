package com.uragiristereo.mejiboard.di

import androidx.room.Room
import com.uragiristereo.mejiboard.data.database.DatabaseRepository
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
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val module = module {
        single {
            return@single Room
                .databaseBuilder(
                    /* context = */ androidContext(),
                    /* klass = */ DatabaseRepository::class.java,
                    /* name = */ "mejiboard-database",
                )
                .build()
        }

        single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }

        viewModel { MainViewModel(get(), get(), get()) }
        viewModel { PostsViewModel(get(), get(), get()) }
        viewModel { SearchViewModel(get(), get()) }
        viewModel { MoreViewModel(get(), get()) }
        viewModel { FiltersViewModel(get(), get()) }
        viewModel { HomeViewModel() }
        viewModel { ImageViewModel(get()) }
        viewModel { MoreBottomSheetViewModel(get(), get()) }
        viewModel { SettingsViewModel(get()) }
    }
}
