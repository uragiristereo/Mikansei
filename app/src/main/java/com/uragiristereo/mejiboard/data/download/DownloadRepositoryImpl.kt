package com.uragiristereo.mejiboard.data.download

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.uragiristereo.mejiboard.R
import com.uragiristereo.mejiboard.common.helper.NumberHelper
import com.uragiristereo.mejiboard.data.download.model.DownloadInfo
import com.uragiristereo.mejiboard.data.download.model.DownloadInstance
import com.uragiristereo.mejiboard.data.download.model.DownloadResource
import com.uragiristereo.mejiboard.data.network.NetworkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import java.io.File

@SuppressLint("MissingPermission")
class DownloadRepositoryImpl(
    private val context: Context,
    private val networkRepository: NetworkRepository,
) : DownloadRepository {
    private val downloads: MutableMap<Int, DownloadInstance> = mutableMapOf()

    private var notificationIdCounter = 0
    private val notificationManager = NotificationManagerCompat.from(context)

    override suspend fun add(postId: Int, url: String) {
        val notificationId = incrementNotificationCounter()

        val cancelDownloadPendingIntent = PendingIntent.getBroadcast(
            /* context = */ context,
            /* requestCode = */ 0,
            /* intent = */
            Intent(
                /* packageContext = */ context,
                /* cls = */ DownloadBroadcastReceiver::class.java,
            ).apply {
                action = postId.toString()
                putExtra(/* name = */ "notificationId", /* value = */ notificationId)
            },
            /* flags = */ PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT,
        )

        val info = DownloadInfo(
            postId = postId,
            url = url,
            notificationId = notificationId,
            notificationBuilder = NotificationCompat.Builder(context, "downloads")
                .setContentTitle("Downloading post $postId")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.close, context.getString(R.string.download_cancel), cancelDownloadPendingIntent),
        )

        val instance = DownloadInstance(
            info = info,
            job = trackDownload(info),
        )

        downloads[postId] = instance
    }

    override fun isPostAlreadyAdded(postId: Int): Boolean {
        return downloads.containsKey(postId)
    }

    override fun remove(id: Int) {
        downloads[id]?.job?.cancel()
        downloads.remove(id)
    }

    private fun incrementNotificationCounter(): Int {
        notificationIdCounter += 1

        return notificationIdCounter
    }

    override fun download(url: String): Flow<DownloadResource> {
        return flow {
            try {
                emit(DownloadResource.Starting)

                val fileName = File(url).name
                val destination = File(context.cacheDir, fileName)
                var length: Long

                val result = networkRepository.api.downloadFile(url)

                result.byteStream().use { inputStream ->
                    destination.outputStream().use { outputStream ->
                        length = result.contentLength()

                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var progress = 0L
                        var bytes = inputStream.read(buffer)

                        while (bytes >= 0) {
                            outputStream.write(buffer, 0, bytes)
                            progress += bytes
                            bytes = inputStream.read(buffer)

                            emit(
                                value = DownloadResource.Downloading(
                                    downloaded = progress,
                                    length = length,
                                    progress = progress.toFloat() / length.toFloat(),
                                ),
                            )
                        }
                    }

                    emit(DownloadResource.Completed(length = length))
                }
            } catch (t: Throwable) {
                emit(DownloadResource.Failed(t))
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun trackDownload(info: DownloadInfo): Job {
        var lastDownloaded = 0L

        return download(info.url)
            .onEach { resource ->
                when (resource) {
                    DownloadResource.Starting -> {
                        notificationManager.apply {
                            info.notificationBuilder
                                .setProgress(
                                    /* max = */ 100,
                                    /* progress = */ 0,
                                    /* indeterminate = */ true,
                                )
                                .setSubText("0% - 0 B/s")
                                .setContentText("0B / 0B")

                            notificationManager.notify(
                                /* id = */ info.notificationId,
                                /* notification = */ info.notificationBuilder.build(),
                            )
                        }
                    }

                    is DownloadResource.Completed -> {
                        val lengthFmt = NumberHelper.convertFileSize(resource.length)

                        notificationManager.apply {
                            info.notificationBuilder
                                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                                .setContentTitle("Post ${info.postId}")
                                .setProgress(
                                    /* max = */ 0,
                                    /* progress = */ 0,
                                    /* indeterminate = */ false,
                                )
                                .setSubText(null)
                                .setContentText("${context.getString(R.string.download_complete)} • $lengthFmt")
                                .setOngoing(false)
                                .setAutoCancel(true)
                                .setOnlyAlertOnce(false)
                                .clearActions()

                            notify(info.notificationId, info.notificationBuilder.build())
                        }
                    }

                    is DownloadResource.Failed -> {
                        notificationManager.apply {
                            info.notificationBuilder
                                .setSmallIcon(R.drawable.error)
                                .setContentTitle(context.getString(R.string.download_post_id, info.postId))
                                .setProgress(
                                    /* max = */ 0,
                                    /* progress = */ 0,
                                    /* indeterminate = */ false,
                                )
                                .setSubText(null)
                                .setContentText(context.getString(R.string.download_failed))
                                .setOngoing(false)
                                .setAutoCancel(true)
                                .setOnlyAlertOnce(false)
                                .clearActions()

                            notify(info.notificationId, info.notificationBuilder.build())
                        }
                    }

                    is DownloadResource.Downloading -> {}
                }
            }
            .sample(periodMillis = 1200L)
            .onEach { resource ->
                if (resource is DownloadResource.Downloading) {
                    val lengthFmt = NumberHelper.convertFileSize(resource.length)
                    val progressPercentage = (resource.progress * 100).toInt()
                    val downloadSpeed = (resource.downloaded - lastDownloaded / 1.2f).toLong()
                    val downloadSpeedFmt = NumberHelper.convertFileSize(downloadSpeed)
                    val downloadedFmt = NumberHelper.convertFileSize(resource.downloaded)

                    notificationManager.apply {
                        info.notificationBuilder
                            .setProgress(
                                /* max = */ 100,
                                /* progress = */ progressPercentage,
                                /* indeterminate = */ resource.downloaded == 0L,
                            )
                            .setSubText("$progressPercentage% - $downloadSpeedFmt/s")
                            .setContentText("$downloadedFmt / $lengthFmt")

                        notificationManager.notify(
                            /* id = */ info.notificationId,
                            /* notification = */ info.notificationBuilder.build(),
                        )
                    }

                    lastDownloaded = resource.downloaded
                }
            }.launchIn(CoroutineScope(Dispatchers.IO))
    }
}
