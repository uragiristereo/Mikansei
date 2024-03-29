package com.uragiristereo.mikansei

import com.uragiristereo.mikansei.core.danbooru.DanbooruRepositoryImpl
import com.uragiristereo.mikansei.core.database.databaseModule
import com.uragiristereo.mikansei.core.domain.DomainModule
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.model.Environment
import com.uragiristereo.mikansei.core.network.NetworkModule
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
import com.uragiristereo.mikansei.core.preferences.PreferencesRepositoryImpl
import com.uragiristereo.mikansei.core.product.ProductModule
import com.uragiristereo.mikansei.feature.filters.FiltersViewModel
import com.uragiristereo.mikansei.feature.home.HomeModule
import com.uragiristereo.mikansei.feature.home.more.MoreViewModel
import com.uragiristereo.mikansei.feature.image.ImageModule
import com.uragiristereo.mikansei.feature.search.SearchViewModel
import com.uragiristereo.mikansei.feature.settings.SettingsViewModel
import com.uragiristereo.mikansei.feature.user.UserModule
import com.uragiristereo.mikansei.ui.MainViewModel
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object MikanseiModule {
    operator fun invoke(): Module = module {
        includes(
            listOf(
                databaseModule(),
                DomainModule(),
                NetworkModule(),
                HomeModule(),
                ImageModule(),
                UserModule(),
                ProductModule(),
            )
        )

        singleOf(::MikanseiEnvironment) { bind<Environment>() }
        single { SupervisorJob() }
        factory { CoroutineScope(context = Dispatchers.Default + get<CompletableJob>()) }

        singleOf(::DanbooruRepositoryImpl) { bind<DanbooruRepository>() }
        singleOf(::PreferencesRepositoryImpl) { bind<PreferencesRepository>() }

        viewModelOf(::MainViewModel)
        viewModelOf(::SearchViewModel)
        viewModelOf(::MoreViewModel)
        viewModelOf(::FiltersViewModel)
        viewModelOf(::SettingsViewModel)
    }
}
