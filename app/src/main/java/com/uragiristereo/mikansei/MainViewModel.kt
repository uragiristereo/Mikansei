package com.uragiristereo.mikansei

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.uragiristereo.safer.compose.navigation.core.route
import com.uragiristereo.mikansei.core.DownloadState
import com.uragiristereo.mikansei.core.database.dao.session.SessionDao
import com.uragiristereo.mikansei.core.domain.usecase.ConvertFileSizeUseCase
import com.uragiristereo.mikansei.core.domain.usecase.DownloadPostUseCase
import com.uragiristereo.mikansei.core.domain.usecase.DownloadPostWithNotificationUseCase
import com.uragiristereo.mikansei.core.download.DownloadRepository
import com.uragiristereo.mikansei.core.download.model.DownloadResource
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.ShareOption
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
import com.uragiristereo.mikansei.core.preferences.model.Preferences
import com.uragiristereo.mikansei.core.resources.R
import com.uragiristereo.mikansei.core.ui.navigation.HomeRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(
    savedStateHandle: SavedStateHandle,
    preferencesRepository: PreferencesRepository,
    sessionDao: SessionDao,
    private val downloadRepository: DownloadRepository,
    private val downloadPostUseCase: DownloadPostUseCase,
    private val downloadPostWithNotificationUseCase: DownloadPostWithNotificationUseCase,
    private val convertFileSizeUseCase: ConvertFileSizeUseCase,
) : ViewModel() {
    var preferences by mutableStateOf(Preferences(theme = preferencesRepository.getInitialTheme()))
        private set

    var confirmExit by mutableStateOf(true)

    var navigatedBackByGesture by mutableStateOf(false)
        private set

    var currentRoute by mutableStateOf(HomeRoute.Posts::class.route)

    var currentTags by mutableStateOf("")
        private set

    private var callbackOnRequestScrollToTop: (() -> Unit) = { }

    private val initialized = savedStateHandle[Constants.STATE_KEY_INITIALIZED] ?: false

    var selectedPost: Post? = null

    // share
    var shareDialogVisible by mutableStateOf(false)

    var downloadState by mutableStateOf(DownloadState())
        private set

    var navigationRailPadding by mutableStateOf(0.dp)

    init {
        if (!initialized) {
            savedStateHandle[Constants.STATE_KEY_INITIALIZED] = true

            viewModelScope.launch(Dispatchers.IO) {
                sessionDao.reset()
            }
        }

        preferencesRepository.flowData.onEach {
            preferences = it
        }.launchIn(viewModelScope)
    }

    @JvmName(name = "setNavigatedBackByGesture2")
    fun setNavigatedBackByGesture(value: Boolean) {
        navigatedBackByGesture = value
    }

    @JvmName(name = "setCurrentTags2")
    fun setCurrentTags(value: String) {
        currentTags = value
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
                downloadPostWithNotificationUseCase(post)
            }

            Toast.makeText(
                /* context = */ context,
                /* text = */ context.getText(R.string.download_added),
                /* duration = */ Toast.LENGTH_LONG,
            ).show()
        }
    }

    fun sharePost(
        context: Context,
        post: Post,
        shareOption: ShareOption,
        onDownloadCompleted: (Uri) -> Unit,
        onDownloadFailed: (String) -> Unit,
    ) {
        selectedPost = post

        val tempDir = File(context.cacheDir, "temp")
            .also { dir ->
                if (!dir.isDirectory) {
                    dir.mkdir()
                }
            }

        val url = when {
            post.hasScaled && shareOption == ShareOption.COMPRESSED -> post.scaledImage.url
            else -> post.image.url
        }

        val fileName = File(url).name
        val uri = File(tempDir.absolutePath, fileName).toUri()

        downloadPostUseCase(
            postId = post.id,
            url = url,
            uri = uri,
        )
            .onEach { resource ->
                when (resource) {
                    DownloadResource.Starting -> {
                        downloadState = DownloadState()

                        // delay to avoid share dialog flashing if the image is already cached
                        delay(timeMillis = 500L)

                        shareDialogVisible = true
                    }

                    is DownloadResource.Downloading -> {
                        val lengthFmt = convertFileSizeUseCase(resource.length)
                        val downloadSpeedFmt = "${convertFileSizeUseCase(resource.speed)}/s"
                        val downloadedFmt = convertFileSizeUseCase(resource.downloaded)

                        downloadState = DownloadState(
                            downloaded = downloadedFmt,
                            fileSize = lengthFmt,
                            progress = resource.progress,
                            downloadSpeed = downloadSpeedFmt,
                        )
                    }

                    is DownloadResource.Completed -> {
                        shareDialogVisible = false

                        onDownloadCompleted(uri)
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
        shareDialogVisible = false
        selectedPost?.let { downloadRepository.remove(it.id) }
    }

    fun setScrollToTopCallback(callback: (() -> Unit)) {
        callbackOnRequestScrollToTop = callback
    }

    fun requestScrollToTop() {
        callbackOnRequestScrollToTop()
    }
}
