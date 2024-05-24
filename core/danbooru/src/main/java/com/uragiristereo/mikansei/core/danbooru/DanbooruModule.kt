package com.uragiristereo.mikansei.core.danbooru

import com.uragiristereo.mikansei.core.danbooru.interceptor.DanbooruAuthInterceptor
import com.uragiristereo.mikansei.core.danbooru.interceptor.DanbooruHostInterceptor
import com.uragiristereo.mikansei.core.danbooru.interceptor.UserDelegationInterceptor
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val danbooruModule = module {
    singleOf(::DanbooruRepositoryImpl) bind DanbooruRepository::class
    singleOf(::DanbooruAuthInterceptor)
    singleOf(::DanbooruHostInterceptor)
    singleOf(::UserDelegationInterceptor)
}
