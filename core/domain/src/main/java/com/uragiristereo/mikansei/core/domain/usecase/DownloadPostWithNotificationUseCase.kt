package com.uragiristereo.mikansei.core.domain.usecase

import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.uragiristereo.mikansei.core.domain.module.network.DownloadBroadcastReceiver
import com.uragiristereo.mikansei.core.domain.module.network.DownloadRepository
import com.uragiristereo.mikansei.core.domain.module.network.entity.DownloadResource
import com.uragiristereo.mikansei.core.model.FileUtil
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.resources.R
import timber.log.Timber
import java.io.File

class DownloadPostWithNotificationUseCase(
    private val context: Context,
    private val downloadRepository: DownloadRepository,
    private val convertFileSizeUseCase: ConvertFileSizeUseCase,
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

        val file = File(
            when (post.type) {
                Post.Type.UGOIRA -> post.medias.scaled!!.url
                else -> post.medias.original.url
            }
        )

        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
        val tempFileUri = File(FileUtil.getTempDir(context), file.name).toUri()

        val values = ContentValues().apply {
            val directory = when {
                mimeType?.contains("video") == true -> Environment.DIRECTORY_MOVIES
                mimeType?.contains("image") == true -> Environment.DIRECTORY_PICTURES
                else -> Environment.DIRECTORY_DOWNLOADS
            }

            put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                directory + File.separator + context.getString(R.string.product_name)
            )
        }

        val location = when {
            mimeType?.contains("video") == true -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            mimeType?.contains("image") == true -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            else -> when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> MediaStore.Downloads.EXTERNAL_CONTENT_URI
                else -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toUri()
            }
        }

        val uri = resolver.insert(location, values)!!

        Timber.d(uri.toString())

        downloadRepository.download(
            postId = post.id,
            url = post.medias.original.url,
            uri = tempFileUri,
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
                        val lengthFmt = convertFileSizeUseCase(resource.length)
                        val progressPercentage = (resource.progress * 100).toInt()
                        val downloadSpeedFmt = convertFileSizeUseCase(resource.speed)
                        val downloadedFmt = convertFileSizeUseCase(resource.downloaded)

                        notificationManager.apply {
                            notificationBuilder
                                .setProgress(
                                    /* max = */ 100,
                                    /* progress = */ progressPercentage,
                                    /* indeterminate = */ resource.downloaded == 0L,
                                )
                                .setSubText("$progressPercentage% • $downloadSpeedFmt/s")
                                .setContentText("$downloadedFmt / $lengthFmt")

                            notificationManager.notify(
                                /* id = */ notificationId,
                                /* notification = */ notificationBuilder.build(),
                            )
                        }
                    }

                    is DownloadResource.Completed -> {
                        FileUtil.copyFile(context = context, sourceUri = tempFileUri, destinationUri = uri)
                        tempFileUri.toFile().delete()

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

                        val lengthFmt = convertFileSizeUseCase(resource.length)

                        notificationManager.apply {
                            notificationBuilder
                                .setSmallIcon(R.drawable.download_done)
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
