package com.uragiristereo.mejiboard.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mejiboard.R
import com.uragiristereo.mejiboard.common.Constants
import com.uragiristereo.mejiboard.data.database.session.SessionDao
import com.uragiristereo.mejiboard.data.download.DownloadRepository
import com.uragiristereo.mejiboard.data.preferences.Preferences
import com.uragiristereo.mejiboard.data.preferences.PreferencesRepository
import com.uragiristereo.mejiboard.domain.entity.source.post.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    preferencesRepository: PreferencesRepository,
    private val downloadRepository: DownloadRepository,
    sessionDao: SessionDao,
) : ViewModel() {
    var preferences by mutableStateOf(Preferences(theme = preferencesRepository.getInitialTheme()))
        private set

    var confirmExit by mutableStateOf(true)

    var navigatedBackByGesture by mutableStateOf(false)

    private val initialized = savedStateHandle[Constants.STATE_KEY_INITIALIZED] ?: false

    var selectedPost: Post? = null

    init {
        if (!initialized) {
            savedStateHandle[Constants.STATE_KEY_INITIALIZED] = true

            viewModelScope.launch(Dispatchers.IO) {
                sessionDao.deleteAllSessions()
            }
        }

        preferencesRepository.flowData.onEach {
            preferences = it
        }.launchIn(viewModelScope)
    }

    fun downloadPost(
        context: Context,
        post: Post,
    ) {
        if (downloadRepository.isPostAlreadyAdded(postId = post.id)) {
            Toast.makeText(
                /* context = */ context,
                /* text = */ context.getText(R.string.download_already_running),
                /* duration = */ Toast.LENGTH_LONG,
            ).show()
        } else {
            viewModelScope.launch {
                downloadRepository.add(
                    postId = post.id,
                    url = post.originalImage.url,
                )
            }

            Toast.makeText(
                /* context = */ context,
                /* text = */ context.getText(R.string.download_added),
                /* duration = */ Toast.LENGTH_LONG,
            ).show()
        }
    }
}
