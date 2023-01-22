package com.uragiristereo.mejiboard.feature.home.more

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mejiboard.core.database.dao.filters.FiltersDao
import com.uragiristereo.mejiboard.core.model.booru.BooruSources
import com.uragiristereo.mejiboard.core.preferences.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoreViewModel(
    preferencesRepository: PreferencesRepository,
    filtersDao: FiltersDao,
) : ViewModel() {
    var preferences by mutableStateOf(preferencesRepository.data)
        private set

    var selectedBooru by mutableStateOf(BooruSources.getBooruByKey(preferences.booru) ?: BooruSources.Gelbooru)
        private set

    var enabledFiltersCount by mutableStateOf(0)

    init {
        preferencesRepository.flowData
            .onEach {
                preferences = it
                selectedBooru = BooruSources.getBooruByKey(it.booru) ?: BooruSources.Gelbooru
            }
            .launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.IO) {
            filtersDao.getEnabledFiltersCount()
                .collect {
                    withContext(Dispatchers.Main) {
                        enabledFiltersCount = it
                    }
                }
        }
    }
}
