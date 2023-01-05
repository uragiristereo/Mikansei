package com.uragiristereo.mejiboard.app

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mejiboard.R
import com.uragiristereo.mejiboard.core.common.data.Constants
import com.uragiristereo.mejiboard.core.common.data.util.NumberUtil
import com.uragiristereo.mejiboard.core.common.ui.entity.DownloadState
import com.uragiristereo.mejiboard.core.database.dao.session.SessionDao
import com.uragiristereo.mejiboard.core.download.DownloadRepository
import com.uragiristereo.mejiboard.core.download.model.DownloadResource
import com.uragiristereo.mejiboard.core.preferences.PreferencesRepository
import com.uragiristereo.mejiboard.core.preferences.model.Preferences
import com.uragiristereo.mejiboard.domain.entity.ShareOption
import com.uragiristereo.mejiboard.domain.entity.booru.post.Post
import com.uragiristereo.mejiboard.domain.usecase.DownloadPostUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    preferencesRepository: PreferencesRepository,
    sessionDao: SessionDao,
    private val downloadRepository: DownloadRepository,
    private val downloadPostUseCase: DownloadPostUseCase,
) : ViewModel() {
    var preferences by mutableStateOf(Preferences(theme = preferencesRepository.getInitialTheme()))
        private set

    var confirmExit by mutableStateOf(true)

    var navigatedBackByGesture by mutableStateOf(false)

    private val initialized = savedStateHandle[Constants.STATE_KEY_INITIALIZED] ?: false

    var selectedPost: Post? = null

    // share
    private var shareJob: Job? = null
    private var lastDownloaded = 0L
    var shareDialogVisible by mutableStateOf(false)

    var downloadState by mutableStateOf(DownloadState())
        private set

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

    fun sharePost(
        post: Post,
        shareOption: ShareOption,
        onDownloadCompleted: () -> Unit,
        onDownloadFailed: (String) -> Unit,
    ) {
        lastDownloaded = 0L

        shareJob = downloadPostUseCase(post, shareOption)
            .onEach { resource ->
                when (resource) {
                    DownloadResource.Starting -> DownloadState()
                    is DownloadResource.Downloading -> {
                        val lengthFmt = NumberUtil.convertFileSize(resource.length)
                        val progressPercentage = (resource.progress * 100).toInt()
                        val downloadSpeed = resource.downloaded - lastDownloaded
                        val downloadSpeedFmt = NumberUtil.convertFileSize(downloadSpeed)
                        val downloadedFmt = NumberUtil.convertFileSize(resource.downloaded)

                        lastDownloaded = resource.downloaded

                        downloadState = DownloadState(
                            downloaded = downloadedFmt,
                            fileSize = lengthFmt,
                            progress = "$progressPercentage%",
                            downloadSpeed = downloadSpeedFmt,
                        )
                    }

                    is DownloadResource.Completed -> {
                        shareDialogVisible = false

                        onDownloadCompleted()
                    }

                    is DownloadResource.Failed -> {
                        shareDialogVisible = false

                        onDownloadFailed(resource.t.toString())
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun cancelShare() {
        shareJob?.cancel()
    }
}
