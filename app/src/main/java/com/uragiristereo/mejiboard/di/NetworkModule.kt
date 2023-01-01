package com.uragiristereo.mejiboard.di

import com.uragiristereo.mejiboard.data.network.NetworkRepositoryImpl
import com.uragiristereo.mejiboard.data.source.BoorusRepositoryImpl
import com.uragiristereo.mejiboard.domain.repository.BoorusRepository
import com.uragiristereo.mejiboard.domain.repository.NetworkRepository
import com.uragiristereo.mejiboard.domain.usecase.GetFileSizeUseCase
import com.uragiristereo.mejiboard.domain.usecase.GetPostsUseCase
import com.uragiristereo.mejiboard.domain.usecase.GetTagsUseCase
import com.uragiristereo.mejiboard.domain.usecase.SearchTermUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object NetworkModule {
    val module = module {
        single<NetworkRepository> { NetworkRepositoryImpl(androidContext(), get()) }
        single<BoorusRepository> { BoorusRepositoryImpl(androidContext(), get(), get()) }

        factory { GetPostsUseCase(get(), get(), get()) }
        factory { SearchTermUseCase(get(), get()) }
        factory { GetTagsUseCase(get()) }
        factory { GetFileSizeUseCase(get()) }
    }
}
