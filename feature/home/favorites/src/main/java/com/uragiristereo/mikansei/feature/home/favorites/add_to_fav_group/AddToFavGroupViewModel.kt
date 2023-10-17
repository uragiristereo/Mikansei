package com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group

import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.uragiristereo.safer.compose.navigation.core.getData
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.domain.usecase.GetFavoriteGroupsUseCase
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.core.FavoriteGroup
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.EmptyCoroutineContext

class AddToFavGroupViewModel(
    savedStateHandle: SavedStateHandle,
    private val danbooruRepository: DanbooruRepository,
    private val getFavoriteGroupsUseCase: GetFavoriteGroupsUseCase,
) : ViewModel() {
    val post = checkNotNull(savedStateHandle.getData<HomeRoute.AddToFavGroup>()).post

    var items by mutableStateOf<List<FavoriteGroup>>(listOf())
        private set

    var isLoading by mutableStateOf(true)
        private set

    var isRemoving by mutableStateOf(false)
        private set

    init {
        loadFavoriteGroups()
    }

    private fun mapResult(items: List<Favorite>): List<FavoriteGroup> {
        return items.map { favorite ->
            FavoriteGroup(
                id = favorite.id,
                name = favorite.name,
                thumbnailUrl = favorite.thumbnailUrl,
                isPostAlreadyExits = favorite.postIds.any { it == post.id },
            )
        }.sortedByDescending { it.isPostAlreadyExits }
    }

    private fun loadFavoriteGroups() {
        viewModelScope.launch {
            isLoading = true

            getFavoriteGroupsUseCase(
                forceCache = true,
                forceRefresh = false,
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        items = mapResult(result.data)
                    }

                    is Result.Failed -> Timber.d(result.message)
                    is Result.Error -> Timber.d(result.t.toString())
                }
            }

            isLoading = false
        }
    }

    fun addPostToFavoriteGroup(
        item: FavoriteGroup,
        onShowMessage: suspend (message: String, length: SnackbarDuration) -> Unit,
    ) {
        viewModelScope.launch(SupervisorJob()) {
            danbooruRepository.addPostToFavoriteGroup(
                favoriteGroupId = item.id,
                postId = post.id
            ).collect { result ->
                val message = when (result) {
                    is Result.Success -> {
                        updateAndCacheFavoriteGroups(showLoading = false)

                        "Added to favorite group: ${item.name}"
                    }

                    is Result.Failed -> "Error: " + result.message
                    is Result.Error -> "Error: " + result.t.toString()
                }

                val duration = when (result) {
                    is Result.Success -> SnackbarDuration.Short
                    else -> SnackbarDuration.Long
                }

                onShowMessage(message, duration)
            }
        }
    }

    private fun updateAndCacheFavoriteGroups(showLoading: Boolean) {
        val context = when {
            showLoading -> EmptyCoroutineContext
            else -> SupervisorJob()
        }

        viewModelScope.launch(context) {
            if (showLoading) {
                isLoading = true
            }

            getFavoriteGroupsUseCase(
                forceCache = true,
                forceRefresh = true,
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        items = mapResult(result.data)

                        Timber.d("updateAndCacheFavoriteGroups success")
                    }

                    is Result.Failed -> Timber.d(result.message)
                    is Result.Error -> Timber.d(result.t.toString())
                }
            }

            if (showLoading) {
                isLoading = false
            }
        }
    }

    fun removePostFromFavoriteGroup(item: FavoriteGroup) {
        viewModelScope.launch {
            isRemoving = true

            danbooruRepository.removePostFromFavoriteGroup(
                favoriteGroupId = item.id,
                postId = post.id
            ).collect { result ->
                when (result) {
                    is Result.Success -> updateAndCacheFavoriteGroups(showLoading = true)
                    is Result.Failed -> Timber.d(result.message)
                    is Result.Error -> Timber.d(result.t.toString())
                }
            }

            isRemoving = false
        }
    }
}
