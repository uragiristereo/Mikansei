package com.uragiristereo.mikansei.feature.saved_searches

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.SavedSearch
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.entity.ImmutableList
import com.uragiristereo.mikansei.core.ui.entity.immutableListOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SavedSearchesViewModel(
    private val danbooruRepository: DanbooruRepository,
) : ViewModel() {
    private val channel = Channel<Event>()
    val event = channel.receiveAsFlow()

    var isLoading by mutableStateOf(false); private set
    var items by mutableStateOf<ImmutableList<SavedSearch>>(immutableListOf()); private set
    var expandedItemId by mutableStateOf<Int?>(null); private set

    init {
        getSavedSearches(refresh = false)
    }

    fun refreshSavedSearches() {
        getSavedSearches(refresh = true)
    }

    fun onExpandedItemIdChange(value: Int?) {
        expandedItemId = value
    }

    fun deleteSavedSearch(id: Int) {
        viewModelScope.launch {
            isLoading = true

            val result = danbooruRepository.deleteSavedSearch(id = id)

            when (result) {
                is Result.Success -> {
                    refreshSavedSearches()
                    channel.send(Event.OnDeleteSuccess)
                }

                is Result.Failed -> {
                    Timber.d(result.message)
                    channel.send(Event.OnFailed(result.message))
                }

                is Result.Error -> {
                    Timber.d((result.t))
                    channel.send(Event.OnFailed(result.t.toString()))
                }
            }

            if (result !is Result.Success) {
                isLoading = false
            }
        }
    }

    private fun getSavedSearches(refresh: Boolean) {
        viewModelScope.launch {
            isLoading = true
            var isFromCache = false
            val result = danbooruRepository.getSavedSearches(forceRefresh = refresh)

            when (result) {
                is Result.Success -> {
                    items = immutableListOf(result.data.items)
                    isFromCache = result.data.isFromCache
                }

                is Result.Failed -> {
                    Timber.d(result.message)
                    channel.send(Event.OnFailed(result.message))
                }

                is Result.Error -> {
                    Timber.d((result.t))
                    channel.send(Event.OnFailed(result.t.toString()))
                }
            }

            if (isFromCache) {
                refreshSavedSearches()
            } else {
                expandedItemId = null
                isLoading = false
            }
        }
    }

    interface Event {
        object OnDeleteSuccess : Event
        data class OnFailed(val message: String) : Event
    }
}
