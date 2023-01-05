package com.uragiristereo.mejiboard.core.booru.di

import com.uragiristereo.mejiboard.core.booru.BooruRepository
import com.uragiristereo.mejiboard.core.booru.BooruRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object BooruModule {
    operator fun invoke(): Module = module {
        singleOf(::BooruRepositoryImpl) { bind<BooruRepository>() }
    }
}
