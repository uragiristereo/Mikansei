package com.uragiristereo.mikansei.feature.user.deactivation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.domain.usecase.DeactivateAccountUseCase
import com.uragiristereo.mikansei.core.model.result.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(SavedStateHandleSaveableApi::class)
class UserDeactivationViewModel(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val danbooruRepository: DanbooruRepository,
    private val deactivateAccountUseCase: DeactivateAccountUseCase,
) : ViewModel() {
    private val channel = Channel<Event>()
    val event = channel.receiveAsFlow()

    val activeUser = userRepository.active
    var isLoading by mutableStateOf(false)
    var showInAppConfirmationDialog by mutableStateOf(false)

    var isConfirming by savedStateHandle.saveable {
        mutableStateOf(false)
    }

    fun deactivateAccount(password: String) {
        viewModelScope.launch {
            isLoading = true

            when (val result = deactivateAccountUseCase(password)) {
                is Result.Success -> channel.send(Event.OnDeactivateSuccess)
                is Result.Failed -> channel.send(Event.OnDeactivateFailed(result.message))
                is Result.Error -> channel.send(Event.OnDeactivateFailed(result.t.toString()))
            }

            isLoading = false
        }
    }

    fun openDeactivationWeb(context: Context) {
        val intent = Intent().apply {
            val baseUrl = danbooruRepository.host.getBaseUrl()
            action = Intent.ACTION_VIEW
            data = "$baseUrl/login?url=/users/${activeUser.value.id}/deactivate".toUri()
        }

        context.startActivity(intent)
    }

    fun switchToAnonymousAndDelete() {
        viewModelScope.launch {
            userRepository.switchToAnonymousAndDelete(activeUser.value)
            channel.send(Event.OnNavigateMore)
        }
    }

    sealed interface Event {
        data object OnDeactivateSuccess : Event
        data class OnDeactivateFailed(val message: String) : Event
        data object OnNavigateMore : Event
    }
}
