package com.uragiristereo.mikansei.feature.user.login.dialog

sealed interface LoginState {
    object Idle : LoginState

    object LoggingIn : LoginState

    object Success : LoginState

    data class Failed(val message: String) : LoginState
}
