package com.uragiristereo.mikansei.saved_searches

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.uragiristereo.mikansei.core.ui.database.SavedSearchItem

class SavedSearchesViewModel(
//    private val savedSearchesDao: SavedSearchesDao,
) : ViewModel() {
//    private val itemsFromDb by lazy { savedSearchesDao.getAll() }

    var items by mutableStateOf<List<SavedSearchItem>>(listOf())
        private set

    init {
//        itemsFromDb
//            .onEach {
//                items = it.toSavedSearchItemList()
//            }
//            .launchIn(viewModelScope)
    }
}
