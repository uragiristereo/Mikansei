package com.uragiristereo.mikansei.feature.user

import com.uragiristereo.mikansei.feature.user.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object UserModule {
    operator fun invoke(): Module = module {
        viewModelOf(::LoginViewModel)
    }
}
