package com.uragiristereo.mikansei.feature.home.favorites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.domain.usecase.GetFavoritesAndFavoriteGroupsUseCase
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.feature.home.favorites.core.LoadingState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val userRepository: UserRepository,
    private val danbooruRepository: DanbooruRepository,
    private val getFavoritesAndFavoriteGroupsUseCase: GetFavoritesAndFavoriteGroupsUseCase,
) : ViewModel() {
    private val channel = Channel<Event>()
    val event = channel.receiveAsFlow()

    var activeUser by mutableStateOf(userRepository.active.value)
        private set

    var loadingState by mutableStateOf(LoadingState.FROM_LOAD)
        private set

    var favorites by mutableStateOf(listOf<Favorite>())
        private set

    init {
        viewModelScope.launch {
            userRepository.active.collect { newUser ->
                when {
                    newUser.isAnonymous() -> favorites = listOf()

                    activeUser.id != newUser.id -> {
                        activeUser = newUser
                        getFavoritesAndFavoriteGroups()
                    }
                }

                activeUser = newUser
            }
        }

        getFavoritesAndFavoriteGroups()
    }

    fun getFavoritesAndFavoriteGroups() {
        if (activeUser.isNotAnonymous()) {
            viewModelScope.launch {
                loadingState = when {
                    favorites.isNotEmpty() -> LoadingState.FROM_REFRESH
                    else -> LoadingState.FROM_LOAD
                }

                when (val result = getFavoritesAndFavoriteGroupsUseCase()) {
                    is Result.Success -> favorites = result.data
                    is Result.Failed -> channel.send(Event.OnError(result.message))
                    is Result.Error -> channel.send(Event.OnError(result.t.toString()))
                }

                loadingState = LoadingState.DISABLED
            }
        }
    }

    fun deleteFavoriteGroup(favoriteGroup: Favorite) {
        loadingState = LoadingState.FROM_REFRESH

        viewModelScope.launch {
            when (val result = danbooruRepository.deleteFavoriteGroup(favoriteGroup.id)) {
                is Result.Success -> {
                    getFavoritesAndFavoriteGroups()
                    channel.send(Event.OnDeleteSuccess)
                }

                is Result.Failed -> channel.send(Event.OnError(result.message))
                is Result.Error -> channel.send(Event.OnError(result.t.toString()))
            }
        }
    }

    sealed interface Event {
        data class OnError(val message: String) : Event
        data object OnDeleteSuccess : Event
    }
}
