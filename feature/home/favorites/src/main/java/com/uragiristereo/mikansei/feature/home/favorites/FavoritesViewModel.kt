package com.uragiristereo.mikansei.feature.home.favorites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.domain.usecase.GetFavoritesAndFavoriteGroupsUseCase
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.feature.home.favorites.core.LoadingState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val userRepository: UserRepository,
    private val getFavoritesAndFavoriteGroupsUseCase: GetFavoritesAndFavoriteGroupsUseCase,
) : ViewModel() {
    var activeUser by mutableStateOf(userRepository.active.value)
        private set

    var loadingState by mutableStateOf(LoadingState.FROM_LOAD)
        private set

    var favorites by mutableStateOf(listOf<Favorite>())
        private set

    val errorMessageChannel = Channel<String>()

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

                getFavoritesAndFavoriteGroupsUseCase().collect { result ->
                    when (result) {
                        is Result.Success -> favorites = result.data
                        is Result.Failed -> errorMessageChannel.send(result.message)
                        is Result.Error -> errorMessageChannel.send(result.t.toString())
                    }
                }

                loadingState = LoadingState.DISABLED
            }
        }
    }
}
