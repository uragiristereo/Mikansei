package com.uragiristereo.mikansei.feature.filters

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.ProfileSettingsField
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.domain.usecase.SyncUserSettingsUseCase
import com.uragiristereo.mikansei.core.domain.usecase.UpdateUserSettingsUseCase
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.feature.filters.column.FilterItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class FiltersViewModel(
    private val userRepository: UserRepository,
    private val updateUserSettingsUseCase: UpdateUserSettingsUseCase,
    private val syncUserSettingsUseCase: SyncUserSettingsUseCase,
) : ViewModel() {
    var username by mutableStateOf(userRepository.active.value.name); private set
    var dialogShown by mutableStateOf(false); private set
    var items by mutableStateOf(listOf<FilterItem>()); private set
    var isLoading by mutableStateOf(false); private set

    private var isUserNotAnonymous = userRepository.active.value.isNotAnonymous()

    val selectedItems by derivedStateOf {
        items.filter { it.selected }
    }

    init {
        viewModelScope.launch {
            userRepository.active.collect { user ->
                username = user.name

                items = user.danbooru.blacklistedTags.map { tag ->
                    FilterItem(tag)
                }

                isUserNotAnonymous = user.isNotAnonymous()
            }
        }

        refreshFilters()
    }

    fun addTags(tags: String) {
        val split = tags
            .lowercase()
            .replace(oldValue = "\r", newValue = "")
            .split("\n")
            .map {
                it.replace("\\s+".toRegex(), " ").trim()
            }
            .distinct()

        val newItems = split.map { item ->
            FilterItem(tags = item)
        }.filter { newItem ->
            newItem.tags !in items.map { it.tags }
        }

        updateUserFilters(updatedItems = items + newItems)
    }

    fun selectAll() {
        items = items.map {
            it.copy(selected = true)
        }
    }

    fun deselectAll() {
        items = items.map {
            it.copy(selected = false)
        }
    }

    fun selectInverse() {
        items = items.map {
            it.copy(selected = !it.selected)
        }
    }

    fun updateSelectedItem(item: FilterItem) {
        items = items.map {
            when (it.tags) {
                item.tags -> item
                else -> it
            }
        }
    }

    fun deleteSelectedItems() {
        val updatedItems = items.filter { item ->
            item.tags !in selectedItems.map { it.tags }
        }

        updateUserFilters(updatedItems)
    }

    fun hideDialog() {
        dialogShown = false
    }

    fun showDialog() {
        dialogShown = true
    }

    private fun updateUserFilters(updatedItems: List<FilterItem>) {
        viewModelScope.launch {
            isLoading = isUserNotAnonymous

            deselectAll()

            val updateUserSettingsResult = updateUserSettingsUseCase(
                ProfileSettingsField(blacklistedTags = updatedItems.map { it.tags })
            )

            when (updateUserSettingsResult) {
                is Result.Success -> Timber.d("updateUserSettings success")
                is Result.Failed -> Timber.d(updateUserSettingsResult.message)
                is Result.Error -> Timber.d(updateUserSettingsResult.t)
            }

            if (isUserNotAnonymous) {
                // delay because the Danbooru API delays the filter updates
                delay(timeMillis = 2_000L)
            }

            when (val syncUserSettingsResult = syncUserSettingsUseCase()) {
                is Result.Success -> Timber.d("syncUserSettings success")
                is Result.Failed -> Timber.d(syncUserSettingsResult.message)
                is Result.Error -> Timber.d(syncUserSettingsResult.t)
            }

            isLoading = false
        }
    }

    fun refreshFilters() {
        viewModelScope.launch {
            isLoading = true

            when (val result = syncUserSettingsUseCase()) {
                is Result.Success -> Timber.d("syncUserSettings success")
                is Result.Failed -> Timber.d(result.message)
                is Result.Error -> Timber.d(result.t)
            }

            isLoading = false
        }
    }
}
