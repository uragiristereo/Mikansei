package com.uragiristereo.mikansei.feature.home.favorites.favgroup.new

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.usecase.GetFavoriteGroupsUseCase
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.home.favorites.favgroup.new.core.FabState
import com.uragiristereo.serializednavigationextension.runtime.navArgsOf
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(SavedStateHandleSaveableApi::class)
class NewFavGroupViewModel(
    savedStateHandle: SavedStateHandle,
    private val danbooruRepository: DanbooruRepository,
    private val getFavoriteGroupsUseCase: GetFavoriteGroupsUseCase,
) : ViewModel() {
    val postId = savedStateHandle.navArgsOf(MainRoute.NewFavGroup()).postId

    private val channel = Channel<Event>()
    val event = channel.receiveAsFlow()

    var textField by savedStateHandle.saveable(
        stateSaver = TextFieldValue.Saver,
        init = {
            mutableStateOf(TextFieldValue())
        },
    )

    private var isLoading by mutableStateOf(false)

    val fabState by derivedStateOf {
        when {
            isLoading -> FabState.LOADING
            textField.text.isBlank() -> FabState.HIDDEN
            else -> FabState.ENABLED
        }
    }

    fun createNewFavoriteGroup() {
        viewModelScope.launch {
            isLoading = true

            danbooruRepository.createNewFavoriteGroup(
                name = textField.text,
                postIds = when {
                    postId != null -> listOf(postId)
                    else -> listOf()
                },
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        Timber.d("createNewFavoriteGroup success, id = ${result.data.id}")

                        updateAndCacheFavoriteGroups()

                        channel.send(Event.Success)
                    }

                    is Result.Failed -> {
                        val message = "Error: ${result.message}"
                        Timber.d(message)

                        channel.send(Event.Failed(message))
                    }

                    is Result.Error -> {
                        val message = "Error: ${result.t}"
                        Timber.d(message)

                        channel.send(Event.Failed(message))
                    }
                }
            }

            isLoading = false
        }
    }

    private fun updateAndCacheFavoriteGroups() {
        viewModelScope.launch(SupervisorJob()) {
            getFavoriteGroupsUseCase(
                forceCache = true,
                forceRefresh = true,
            ).collect()
        }
    }

    sealed interface Event {
        data object Success : Event

        data class Failed(val message: String) : Event
    }
}
