package com.uragiristereo.mikansei.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.domain.module.database.PostRepository
import com.uragiristereo.mikansei.core.domain.module.database.SessionRepository
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.FileUtil
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
import com.uragiristereo.mikansei.core.product.shared.downloadshare.DownloadShareViewModel
import com.uragiristereo.mikansei.core.product.shared.downloadshare.DownloadShareViewModelImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    preferencesRepository: PreferencesRepository,
    sessionRepository: SessionRepository,
    postRepository: PostRepository,
    private val applicationContext: Context,
) : ViewModel(), DownloadShareViewModel by DownloadShareViewModelImpl() {
    val preferences = preferencesRepository.data

    var confirmExit by mutableStateOf(true)

    var currentTags by mutableStateOf("")
        private set

    private val initialized = savedStateHandle[Constants.STATE_KEY_INITIALIZED] ?: false

    var navigationRailPadding by mutableStateOf(0.dp)

    val scrollToTopChannel = Channel<String>()
    val testMode = preferences.value.testMode

    init {
        if (!initialized) {
            savedStateHandle[Constants.STATE_KEY_INITIALIZED] = true

            viewModelScope.launch(Dispatchers.IO) {
                postRepository.reset()
                sessionRepository.reset()
            }
        }

        removeTempFiles()
    }

    @JvmName(name = "setCurrentTags2")
    fun setCurrentTags(value: String) {
        currentTags = value
    }

    private fun removeTempFiles() {
        viewModelScope.launch {
            FileUtil.getTempDir(applicationContext).deleteRecursively()
        }
    }
}
