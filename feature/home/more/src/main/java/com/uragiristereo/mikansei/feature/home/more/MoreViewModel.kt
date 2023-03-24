package com.uragiristereo.mikansei.feature.home.more

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.model.booru.BooruSource
import com.uragiristereo.mikansei.core.model.booru.getBooruByKey
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MoreViewModel(
    preferencesRepository: PreferencesRepository,
) : ViewModel() {
    var preferences by mutableStateOf(preferencesRepository.data)
        private set

    var selectedBooru by mutableStateOf(BooruSource.values().getBooruByKey(preferences.booru) ?: BooruSource.Gelbooru)
        private set

    var enabledFiltersCount by mutableStateOf(0)

    init {
        preferencesRepository.flowData
            .onEach {
                preferences = it
                selectedBooru = BooruSource.values().getBooruByKey(it.booru) ?: BooruSource.Gelbooru
            }
            .launchIn(viewModelScope)

//        viewModelScope.launch(Dispatchers.IO) {
//            filtersDao.getEnabledFiltersCount()
//                .collect {
//                    withContext(Dispatchers.Main) {
//                        enabledFiltersCount = it
//                    }
//                }
//        }
    }
}
