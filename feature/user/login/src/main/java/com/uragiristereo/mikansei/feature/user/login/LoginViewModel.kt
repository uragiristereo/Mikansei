package com.uragiristereo.mikansei.feature.user.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.uragiristereo.mikansei.core.domain.usecase.PerformLoginUseCase
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.feature.user.login.dialog.LoginState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(SavedStateHandleSaveableApi::class)
class LoginViewModel(
    savedStateHandle: SavedStateHandle,
    private val performLoginUseCase: PerformLoginUseCase,
) : ViewModel() {
    var isApiKeyVisible by savedStateHandle.saveable {
        mutableStateOf(false)
    }
        private set

    var loginState by mutableStateOf<LoginState>(LoginState.Idle)
        private set

    var job: Job? = null

    fun toggleApiKeyVisible() {
        isApiKeyVisible = !isApiKeyVisible
    }

    fun performLogin(name: String, apiKey: String) {
        job = viewModelScope.launch {
            loginState = LoginState.LoggingIn

            performLoginUseCase(name, apiKey)
                .collect { result ->
                    loginState = when (result) {
                        is Result.Success -> LoginState.Success
                        is Result.Failed -> LoginState.Failed(message = result.message)
                        is Result.Error -> LoginState.Failed(message = result.t.toString())
                    }
                }
        }
    }

    fun onDialogDismiss() {
        loginState = LoginState.Idle
    }

    fun cancelLogin() {
        job?.cancel()
        loginState = LoginState.Idle
    }
}
