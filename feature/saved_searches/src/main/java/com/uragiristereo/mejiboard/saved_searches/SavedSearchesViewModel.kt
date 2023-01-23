package com.uragiristereo.mejiboard.saved_searches

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mejiboard.core.database.dao.saved_searches.SavedSearchesDao
import com.uragiristereo.mejiboard.core.database.dao.saved_searches.toSavedSearchItemList
import com.uragiristereo.mejiboard.core.ui.database.SavedSearchItem
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SavedSearchesViewModel(
    private val savedSearchesDao: SavedSearchesDao,
) : ViewModel() {
    private val itemsFromDb by lazy { savedSearchesDao.getAll() }

    var items by mutableStateOf<List<SavedSearchItem>>(listOf())
        private set

    init {
        itemsFromDb
            .onEach {
                items = it.toSavedSearchItemList()
            }
            .launchIn(viewModelScope)
    }
}
