package com.uragiristereo.mikansei.feature.home.favorites.new_fav_group

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.uragiristereo.safer.compose.navigation.core.getData
import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.usecase.GetFavoriteGroupsUseCase
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.navigation.MainRoute
import com.uragiristereo.mikansei.feature.home.favorites.new_fav_group.core.NewFavGroupState
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class NewFavGroupViewModel(
    savedStateHandle: SavedStateHandle,
    private val danbooruRepository: DanbooruRepository,
    private val getFavoriteGroupsUseCase: GetFavoriteGroupsUseCase,
) : ViewModel() {
    val postId = savedStateHandle.getData(MainRoute.NewFavGroup()).postId

    var isLoading by mutableStateOf(false)
        private set

    val channel = Channel<NewFavGroupState>()

    fun createNewFavoriteGroup(name: String) {
        viewModelScope.launch {
            isLoading = true

            danbooruRepository.createNewFavoriteGroup(
                name = name,
                postIds = when {
                    postId != null -> listOf(postId)
                    else -> listOf()
                },
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        Timber.d("createNewFavoriteGroup success, id = ${result.data.id}")

                        updateAndCacheFavoriteGroups()

                        channel.send(NewFavGroupState.Success)
                    }

                    is Result.Failed -> {
                        val message = "Error: ${result.message}"
                        Timber.d(message)

                        channel.send(NewFavGroupState.Failed(message))
                    }

                    is Result.Error -> {
                        val message = "Error: ${result.t}"
                        Timber.d(message)

                        channel.send(NewFavGroupState.Failed(message))
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
}
