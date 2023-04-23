package com.uragiristereo.mikansei.feature.home

import com.uragiristereo.mikansei.feature.home.favorites.FavoritesViewModel
import com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.AddToFavGroupViewModel
import com.uragiristereo.mikansei.feature.home.posts.post_dialog.PostDialogViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object HomeModule {
    operator fun invoke(): Module = module {
        viewModelOf(::FavoritesViewModel)
        viewModelOf(::PostDialogViewModel)
        viewModelOf(::AddToFavGroupViewModel)
    }
}
