package com.uragiristereo.mikansei.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.uragiristereo.mikansei.core.database.session.SessionDao
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
import com.uragiristereo.mikansei.core.product.shared.downloadshare.DownloadShareViewModel
import com.uragiristereo.mikansei.core.product.shared.downloadshare.DownloadShareViewModelImpl
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    preferencesRepository: PreferencesRepository,
    sessionDao: SessionDao,
) : ViewModel(), DownloadShareViewModel by DownloadShareViewModelImpl() {
    val preferences = preferencesRepository.data

    var confirmExit by mutableStateOf(true)

    val navigatedBackByGesture = mutableStateOf(false)

    var currentRoute by mutableStateOf(HomeRoute.Posts::class.route)

    var currentTags by mutableStateOf("")
        private set

    private val initialized = savedStateHandle[Constants.STATE_KEY_INITIALIZED] ?: false

    var navigationRailPadding by mutableStateOf(0.dp)

    val scrollToTopChannel = Channel<String>()

    init {
        if (!initialized) {
            savedStateHandle[Constants.STATE_KEY_INITIALIZED] = true

            viewModelScope.launch(Dispatchers.IO) {
                sessionDao.reset()
            }
        }
    }

    @JvmName(name = "setNavigatedBackByGesture2")
    fun setNavigatedBackByGesture(value: Boolean) {
        Timber.d("navigatedBackByGesture $value")
        navigatedBackByGesture.value = value
    }

    @JvmName(name = "setCurrentTags2")
    fun setCurrentTags(value: String) {
        currentTags = value
    }
}
