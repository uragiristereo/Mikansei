package com.uragiristereo.mikansei.feature.filters

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.ui.database.FilterItem
import com.uragiristereo.mikansei.core.ui.extension.strip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FiltersViewModel() : ViewModel() {
    var dialogShown by mutableStateOf(false)
        private set

    var items by mutableStateOf(listOf<FilterItem>())
        private set

    val selectedItems by derivedStateOf {
        items.filter { it.selected }
    }

    init {
//        itemsFromDb.onEach {
//            items = it.toFilterItemList()
//        }.launchIn(viewModelScope)
    }

    fun addTags(
        context: Context,
        tags: String,
    ) {
        viewModelScope.launch {
            val split = tags
                .strip(splitter = "")
                .split(' ')

            val newItems = split.map { item ->
                FilterItem(tag = item)
            }.filter { newItem ->
                newItem.tag !in items.map { it.tag }
            }

//            filtersDao.insert(newItems.toFilterTableItemList())

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

//            filtersDao.update(item.toFilterTableItem())
        }
    }

    fun deleteSelectedItems() {
        viewModelScope.launch(Dispatchers.IO) {
//            filtersDao.deleteItems(selectedItems.toFilterTableItemList())
        }
    }

//    fun onToggleChecked(checked: Boolean) {
//        viewModelScope.launch {
//            toggleChecked = checked
//
//            preferencesRepository.update(preferences.copy(filtersEnabled = checked))
//        }
//    }

    fun changeDialogState(value: Boolean) {
        dialogShown = value
    }

    fun showDialog() {
        dialogShown = true
    }
}
