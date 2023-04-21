package com.uragiristereo.mikansei.core.product.shared.downloadshare

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uragiristereo.mikansei.core.domain.usecase.ConvertFileSizeUseCase
import com.uragiristereo.mikansei.core.domain.usecase.DownloadPostUseCase
import com.uragiristereo.mikansei.core.domain.usecase.DownloadPostWithNotificationUseCase
import com.uragiristereo.mikansei.core.download.DownloadRepository
import com.uragiristereo.mikansei.core.download.model.DownloadResource
import com.uragiristereo.mikansei.core.model.ShareOption
import com.uragiristereo.mikansei.core.model.danbooru.post.Post
import com.uragiristereo.mikansei.core.product.shared.downloadshare.core.DownloadState
import com.uragiristereo.mikansei.core.resources.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import java.io.File

open class DownloadShareViewModelImpl : ViewModel(), DownloadShareViewModel, KoinComponent {
    private val downloadRepository: DownloadRepository by inject()
    private val downloadPostUseCase: DownloadPostUseCase by inject()
    private val downloadPostWithNotificationUseCase: DownloadPostWithNotificationUseCase by inject()
    private val convertFileSizeUseCase: ConvertFileSizeUseCase by inject()

    // share
    override var shareDialogVisible by mutableStateOf(false)

    override var downloadState by mutableStateOf(DownloadState())

    override var selectedPost: Post? = null

    override fun downloadPost(
        context: Context,
        post: Post,
    ) {
        if (downloadRepository.isPostAlreadyAdded(postId = post.id)) {
            Toast.makeText(context, context.getText(R.string.download_already_running), Toast.LENGTH_LONG).show()
        } else {
            viewModelScope.launch {
                downloadPostWithNotificationUseCase(post)
            }

            Toast.makeText(context, context.getText(R.string.download_added), Toast.LENGTH_SHORT).show()
        }
    }

    override fun sharePost(
        context: Context,
        post: Post,
        shareOption: ShareOption,
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
        ).onEach { resource ->
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

                    Timber.d("download completed")

                    val uriProvider = FileProvider.getUriForFile(context, "${context.packageName}.provider", uri.toFile())

                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_STREAM, uriProvider)
                        clipData = ClipData.newRawUri(null, uriProvider)
                        type = context.contentResolver.getType(uriProvider)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }

                    context.startActivity(
                        Intent.createChooser(intent, context.getString(R.string.share_label)),
                    )
                }

                is DownloadResource.Failed -> {
                    shareDialogVisible = false

                    Timber.d("download failed = ${resource.t.toString()}")
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun cancelShare() {
        shareDialogVisible = false
        selectedPost?.let { downloadRepository.remove(it.id) }
    }
}