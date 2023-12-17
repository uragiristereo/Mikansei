package com.uragiristereo.mikansei.feature.home

import com.uragiristereo.mikansei.feature.home.favorites.FavoritesViewModel
import com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.AddToFavGroupViewModel
import com.uragiristereo.mikansei.feature.home.favorites.new_fav_group.NewFavGroupViewModel
import com.uragiristereo.mikansei.feature.home.posts.PostsViewModel
import com.uragiristereo.mikansei.feature.home.posts.more.PostMoreViewModel
import com.uragiristereo.mikansei.feature.home.posts.share.ShareViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object HomeModule {
    operator fun invoke(): Module = module {
        viewModelOf(::PostsViewModel)
        viewModelOf(::FavoritesViewModel)
        viewModelOf(::PostMoreViewModel)
        viewModelOf(::AddToFavGroupViewModel)
        viewModelOf(::NewFavGroupViewModel)
        viewModelOf(::ShareViewModel)
    }
}
