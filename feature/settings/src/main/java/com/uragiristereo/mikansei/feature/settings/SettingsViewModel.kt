package com.uragiristereo.mikansei.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.model.Environment
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
import com.uragiristereo.mikansei.core.preferences.model.Preferences
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesRepository: PreferencesRepository,
    environment: Environment,
) : ViewModel() {
    val preferences = preferencesRepository.data
    val debugEnvironment = environment.debug

    fun updatePreferences(data: (Preferences) -> Preferences) {
        viewModelScope.launch {
            preferencesRepository.update(data)
        }
    }
}
