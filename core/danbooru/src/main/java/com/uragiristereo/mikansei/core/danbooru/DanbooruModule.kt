package com.uragiristereo.mikansei.core.danbooru


import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object DanbooruModule {
    operator fun invoke(): Module = module {
        singleOf(::DanbooruRepositoryImpl) { bind<DanbooruRepository>() }
    }
}
