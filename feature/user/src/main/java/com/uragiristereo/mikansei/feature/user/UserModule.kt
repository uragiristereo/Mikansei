package com.uragiristereo.mikansei.feature.user

import com.uragiristereo.mikansei.feature.user.deactivation.UserDeactivationViewModel
import com.uragiristereo.mikansei.feature.user.delegation.UserDelegationSettingsViewModel
import com.uragiristereo.mikansei.feature.user.login.LoginViewModel
import com.uragiristereo.mikansei.feature.user.manage.ManageUserViewModel
import com.uragiristereo.mikansei.feature.user.settings.UserSettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object UserModule {
    operator fun invoke(): Module = module {
        viewModelOf(::LoginViewModel)
        viewModelOf(::ManageUserViewModel)
        viewModelOf(::UserSettingsViewModel)
        viewModelOf(::UserDelegationSettingsViewModel)
        viewModelOf(::UserDeactivationViewModel)
    }
}
