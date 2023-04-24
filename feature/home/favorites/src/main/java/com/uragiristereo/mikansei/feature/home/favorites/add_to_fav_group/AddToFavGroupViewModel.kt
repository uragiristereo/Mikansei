package com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group

import android.content.Context
import android.widget.Toast
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
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import com.uragiristereo.mikansei.feature.home.favorites.add_to_fav_group.column.FavoriteGroup
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class AddToFavGroupViewModel(
    savedStateHandle: SavedStateHandle,
    private val danbooruRepository: DanbooruRepository,
    private val getFavoriteGroupsUseCase: GetFavoriteGroupsUseCase,
) : ViewModel() {
    private val post = checkNotNull(savedStateHandle.getData<HomeRoute.AddToFavGroup>()).post

    var items by mutableStateOf<List<FavoriteGroup>>(listOf())
        private set

    var isLoading by mutableStateOf(true)
        private set

    init {
        loadFavoriteGroups(refresh = false)
    }

    private fun loadFavoriteGroups(refresh: Boolean) {
        viewModelScope.launch {
            isLoading = true

            getFavoriteGroupsUseCase(
                forceCache = true,
                forceRefresh = refresh,
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        items = result.data.map { favorite ->
                            FavoriteGroup(
                                id = favorite.id,
                                name = favorite.name,
                                thumbnailUrl = favorite.thumbnailUrl,
                                isPostAlreadyExits = favorite.postIds.any { it == post.id },
                            )
                        }
                    }

                    is Result.Failed -> Timber.d(result.message)
                    is Result.Error -> Timber.d(result.t.toString())
                }
            }

            isLoading = false
        }
    }

    fun refreshFavoriteGroups() {
        loadFavoriteGroups(refresh = true)
    }

    fun addPostToFavoriteGroup(context: Context, item: FavoriteGroup) {
        viewModelScope.launch(SupervisorJob()) {
            danbooruRepository.addPostToFavoriteGroup(
                favoriteGroupId = item.id,
                postId = post.id
            ).collect { result ->
                val message = when (result) {
                    is Result.Success -> {
                        val itemIsNotFirst = item.id != items.firstOrNull()?.id

                        if (itemIsNotFirst) {
                            updateAndCacheFavoriteGroups()
                        }

                        "Added to favorite group: ${item.name}"
                    }

                    is Result.Failed -> "Error: " + result.message
                    is Result.Error -> "Error: " + result.t.toString()
                }

                val duration = when (result) {
                    is Result.Success -> Toast.LENGTH_SHORT
                    else -> Toast.LENGTH_LONG
                }

                Toast.makeText(context, message, duration).show()
            }
        }
    }

    private fun updateAndCacheFavoriteGroups() {
        viewModelScope.launch(SupervisorJob()) {
            getFavoriteGroupsUseCase(forceCache = true, forceRefresh = true).collect()
        }
    }
}