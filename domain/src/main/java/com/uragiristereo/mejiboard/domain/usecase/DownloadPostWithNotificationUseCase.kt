package com.uragiristereo.mejiboard.domain.usecase

import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.uragiristereo.mejiboard.core.common.data.util.NumberUtil
import com.uragiristereo.mejiboard.core.download.DownloadBroadcastReceiver
import com.uragiristereo.mejiboard.core.download.DownloadRepository
import com.uragiristereo.mejiboard.core.download.model.DownloadResource
import com.uragiristereo.mejiboard.core.model.booru.post.Post
import com.uragiristereo.mejiboard.core.resources.R
import timber.log.Timber
import java.io.File

class DownloadPostWithNotificationUseCase(
    private val context: Context,
    private val downloadRepository: DownloadRepository,
) {
    private val resolver = context.contentResolver

    suspend operator fun invoke(post: Post) {
        val notificationId = downloadRepository.incrementNotificationCounter()
        val notificationManager = NotificationManagerCompat.from(context)

        val cancelDownloadPendingIntent = PendingIntent.getBroadcast(
            /* context = */ context,
            /* requestCode = */ 0,
            /* intent = */
            Intent(
                /* packageContext = */ context,
                /* cls = */ DownloadBroadcastReceiver::class.java,
            ).apply {
                action = post.id.toString()
                putExtra(/* name = */ "notificationId", /* value = */ notificationId)
            },
            /* flags = */ PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT,
        )

        val notificationBuilder = NotificationCompat.Builder(context, "downloads")
            .setContentTitle(context.getString(R.string.download_in_progress, post.id))
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.close, context.getString(R.string.download_cancel), cancelDownloadPendingIntent)

        val file = File(post.originalImage.url)

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
            put(MediaStore.MediaColumns.MIME_TYPE, MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension))
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + context.getString(R.string.app_name_alt))
        }

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

        Timber.d(uri.toString())

        downloadRepository.download(
            postId = post.id,
            url = post.originalImage.url,
            uri = uri,
            sample = 1200L,
        )
            .collect { resource ->
                when (resource) {
                    DownloadResource.Starting -> {
                        notificationManager.apply {
                            notificationBuilder
                                .setProgress(
                                    /* max = */ 100,
                                    /* progress = */ 0,
                                    /* indeterminate = */ true,
                                )
                                .setSubText("0% - 0 B/s")
                                .setContentText("0B / 0B")

                            notificationManager.notify(
                                /* id = */ notificationId,
                                /* notification = */ notificationBuilder.build(),
                            )
                        }
                    }

                    is DownloadResource.Downloading -> {
                        val lengthFmt = NumberUtil.convertFileSize(resource.length)
                        val progressPercentage = (resource.progress * 100).toInt()
                        val downloadSpeedFmt = NumberUtil.convertFileSize(resource.speed)
                        val downloadedFmt = NumberUtil.convertFileSize(resource.downloaded)

                        notificationManager.apply {
                            notificationBuilder
                                .setProgress(
                                    /* max = */ 100,
                                    /* progress = */ progressPercentage,
                                    /* indeterminate = */ resource.downloaded == 0L,
                                )
                                .setSubText("$progressPercentage% - $downloadSpeedFmt/s")
                                .setContentText("$downloadedFmt / $lengthFmt")

                            notificationManager.notify(
                                /* id = */ notificationId,
                                /* notification = */ notificationBuilder.build(),
                            )
                        }
                    }

                    is DownloadResource.Completed -> {
                        val openDownloadedFileIntent = Intent().apply {
                            action = Intent.ACTION_VIEW
                            setDataAndType(uri, resolver.getType(uri))
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }

                        val pendingOpenDownloadedFileIntent = PendingIntent.getActivity(
                            /* context = */ context,
                            /* requestCode = */ 0,
                            /* intent = */ openDownloadedFileIntent,
                            /* flags = */ PendingIntent.FLAG_IMMUTABLE
                        )

                        val lengthFmt = NumberUtil.convertFileSize(resource.length)

                        notificationManager.apply {
                            notificationBuilder
                                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                                .setContentTitle(context.getString(R.string.download_post_id, post.id))
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
                                .setContentIntent(pendingOpenDownloadedFileIntent)
                                .clearActions()

                            notificationManager.notify(
                                /* id = */ notificationId,
                                /* notification = */ notificationBuilder.build(),
                            )
                        }
                    }

                    is DownloadResource.Failed -> {
                        resource.t?.printStackTrace()

                        notificationManager.apply {
                            notificationBuilder
                                .setSmallIcon(R.drawable.error)
                                .setContentTitle(context.getString(R.string.download_post_id, post.id))
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

                            notificationManager.notify(
                                /* id = */ notificationId,
                                /* notification = */ notificationBuilder.build(),
                            )
                        }
                    }
                }
            }
    }
}
