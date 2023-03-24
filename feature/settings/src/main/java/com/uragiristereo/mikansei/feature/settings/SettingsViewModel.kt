package com.uragiristereo.mikansei.feature.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
import com.uragiristereo.mikansei.core.preferences.model.Preferences
import com.uragiristereo.mikansei.core.preferences.model.RatingPreference
import com.uragiristereo.mikansei.core.preferences.model.base.Preference
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.feature.settings.preference.BottomSheetPreferenceData
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {
    var preferences by mutableStateOf(Preferences())
        private set

    init {
        preferencesRepository.flowData.onEach {
            preferences = it
        }.launchIn(viewModelScope)
    }

    fun updatePreferences(data: (Preferences) -> Preferences) {
        viewModelScope.launch {
            preferencesRepository.update(data(preferences))
        }
    }

//    var booruSources by mutableStateOf(
//        value = BottomSheetPreferenceData(
//            preferenceKey = "booru_sources",
//            preferenceTextResId = R.string.settings_default_booru_source_select,
//            items = BooruSource.values().map { it.toPreferenceItem() },
//            selectedItem = BooruSource.values().getBooruByKey(preferences.booru)?.toPreferenceItem() ?: BooruSource.Gelbooru.toPreferenceItem(),
//        ),
//    )
//        private set

    var ratingFilters by mutableStateOf(
        value = BottomSheetPreferenceData(
            preferenceTextResId = R.string.settings_booru_listing_mode_select,
            items = RatingPreference.values().toList(),
            selectedItem = preferences.ratingFilters,
        ),
    )
        private set

    fun setBottomSheetPreferenceState(preference: Preference) {
        when (preference) {
//            booruSources.preferenceKey -> {
//                booruSources = booruSources.copy(selectedItem = selectedItem)
//
//                updatePreferences {
//                    it.copy(
//                        booru = booruSources.selectedItem?.key ?: BooruSource.Gelbooru.key,
//                    )
//                }
//            }

            is RatingPreference -> {
                ratingFilters = ratingFilters.copy(selectedItem = preference)

                updatePreferences { it.copy(ratingFilters = preference) }
            }
        }
    }
}
