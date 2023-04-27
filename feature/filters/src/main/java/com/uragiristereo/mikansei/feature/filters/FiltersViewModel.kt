package com.uragiristereo.mikansei.feature.filters

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.database.dao.user.toUser
import com.uragiristereo.mikansei.core.domain.entity.user.UserField
import com.uragiristereo.mikansei.core.domain.usecase.SyncUserSettingsUseCase
import com.uragiristereo.mikansei.core.domain.usecase.UpdateUserSettingsUseCase
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.ui.extension.strip
import com.uragiristereo.mikansei.feature.filters.column.FilterItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import timber.log.Timber

class FiltersViewModel(
    userDao: UserDao,
    private val updateUserSettingsUseCase: UpdateUserSettingsUseCase,
    private val syncUserSettingsUseCase: SyncUserSettingsUseCase,
) : ViewModel() {
    var username by mutableStateOf<String?>(null)
        private set

    var dialogShown by mutableStateOf(false)
        private set

    var items by mutableStateOf(listOf<FilterItem>())
        private set

    val selectedItems by derivedStateOf {
        items.filter { it.selected }
    }

    var isLoading by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            userDao.getActive().collect { userRow ->
                val user = userRow.toUser()
                username = user.name

                items = user.blacklistedTags.map { tag ->
                    FilterItem(tag)
                }
            }
        }

        refreshFilters()
    }

    fun addTags(tags: String) {
        val split = tags.strip(splitter = "").split(' ').distinct()

        val newItems = split.map { item ->
            FilterItem(tag = item)
        }.filter { newItem ->
            newItem.tag !in items.map { it.tag }
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
            when (it.tag) {
                item.tag -> item
                else -> it
            }
        }
    }

    fun deleteSelectedItems() {
        val updatedItems = items.filter { item ->
            item.tag !in selectedItems.map { it.tag }
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
            isLoading = true

            deselectAll()

            val updateUserFlow = updateUserSettingsUseCase(
                UserField(blacklistedTags = updatedItems.map { it.tag })
            )

            val delayFlow = flow<Result<Unit>> {
                delay(timeMillis = 1000L)
            }

            merge(updateUserFlow, delayFlow, syncUserSettingsUseCase()).collect { result ->
                when (result) {
                    is Result.Success -> Timber.d("updateUserSettings & syncUserSettings success")
                    is Result.Failed -> Timber.d(result.message)
                    is Result.Error -> Timber.d(result.t)
                }
            }

            isLoading = false
        }
    }

    fun refreshFilters() {
        viewModelScope.launch {
            isLoading = true

            syncUserSettingsUseCase().collect { result ->
                when (result) {
                    is Result.Success -> Timber.d("syncUserSettings success")
                    is Result.Failed -> Timber.d(result.message)
                    is Result.Error -> Timber.d(result.t)
                }
            }

            isLoading = false
        }
    }
}
