package com.uragiristereo.mejiboard.presentation.filters

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mejiboard.data.database.DatabaseRepository
import com.uragiristereo.mejiboard.data.database.filters.toFilterItemList
import com.uragiristereo.mejiboard.data.database.filters.toFilterTableItem
import com.uragiristereo.mejiboard.data.database.filters.toFilterTableItemList
import com.uragiristereo.mejiboard.data.preferences.Preferences
import com.uragiristereo.mejiboard.domain.repository.PreferencesRepository
import com.uragiristereo.mejiboard.presentation.common.entity.FilterItem
import com.uragiristereo.mejiboard.presentation.common.extension.strip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FiltersViewModel(
    databaseRepository: DatabaseRepository,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {
    private val dao = databaseRepository.filtersDao()
    private val itemsFromDb by lazy { dao.getAll() }

    private val preferences: Preferences
        get() = preferencesRepository.data


    var toggleChecked by mutableStateOf(preferences.filtersEnabled)

    var dialogShown by mutableStateOf(false)
        private set

    var items by mutableStateOf(listOf<FilterItem>())
        private set

    val selectedItems by derivedStateOf {
        items.filter { it.selected }
    }

    init {
        itemsFromDb.onEach {
            items = it.toFilterItemList()
        }.launchIn(viewModelScope)
    }

    fun addTags(
        context: Context,
        tags: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val split = tags
                .strip(splitter = "")
                .split(' ')

            val newItems = split.map { item ->
                FilterItem(tag = item)
            }.filter { newItem ->
                newItem.tag !in items.map { it.tag }
            }

            dao.insert(newItems.toFilterTableItemList())

            val toastMessage = when {
                newItems.size == 1 -> "1 tag added successfully!"
                newItems.isNotEmpty() -> "${newItems.size} tag(s) added successfully!"
                else -> "No tags have been added."
            }

            launch(Dispatchers.Main) {
                Toast.makeText(
                    /* context = */ context,
                    /* text = */ toastMessage,
                    /* duration = */ Toast.LENGTH_LONG,
                ).show()
            }
        }
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
            when (it.id) {
                item.id -> item
                else -> it
            }
        }
    }

    fun updateItem(item: FilterItem) {
        viewModelScope.launch(Dispatchers.IO) {
            updateSelectedItem(item)

            dao.update(item.toFilterTableItem())
        }
    }

    fun deleteSelectedItems() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteItems(selectedItems.toFilterTableItemList())
        }
    }

    fun onToggleChecked(checked: Boolean) {
        viewModelScope.launch {
            toggleChecked = checked

            preferencesRepository.update(preferences.copy(filtersEnabled = checked))
        }
    }

    fun changeDialogState(value: Boolean) {
        dialogShown = value
    }

    fun showDialog() {
        dialogShown = true
    }
}
