package com.uragiristereo.mejiboard.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mejiboard.common.Constants
import com.uragiristereo.mejiboard.data.database.DatabaseRepository
import com.uragiristereo.mejiboard.data.preferences.Preferences
import com.uragiristereo.mejiboard.domain.repository.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    databaseRepository: DatabaseRepository,
    preferencesRepository: PreferencesRepository,
) : ViewModel() {
    var preferences by mutableStateOf(Preferences(theme = preferencesRepository.getInitialTheme()))
        private set

    var confirmExit by mutableStateOf(true)

    var navigatedBackByGesture by mutableStateOf(false)

    private val initialized = savedStateHandle[Constants.STATE_KEY_INITIALIZED] ?: false

    init {
        if (!initialized) {
            savedStateHandle[Constants.STATE_KEY_INITIALIZED] = true

            viewModelScope.launch(Dispatchers.IO) {
                databaseRepository.sessionDao().deleteAllSession()
            }
        }

        preferencesRepository.flowData.onEach {
            preferences = it
        }.launchIn(viewModelScope)
    }
}
