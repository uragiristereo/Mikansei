package com.uragiristereo.mikansei.feature.home

import com.uragiristereo.mikansei.feature.home.favorites.FavoritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object HomeModule {
    operator fun invoke(): Module = module {
        viewModelOf(::FavoritesViewModel)
    }
}
