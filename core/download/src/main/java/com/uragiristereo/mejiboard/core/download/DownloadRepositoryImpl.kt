package com.uragiristereo.mejiboard.core.download

import android.annotation.SuppressLint
import android.content.Context
import com.uragiristereo.mejiboard.core.download.model.DownloadResource
import com.uragiristereo.mejiboard.core.preferences.NetworkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File

@SuppressLint("MissingPermission")
class DownloadRepositoryImpl(
    private val context: Context,
    private val networkRepository: NetworkRepository,
) : DownloadRepository {
    private val downloads: MutableMap<Int, Job> = mutableMapOf()
    private var notificationIdCounter = 0

    private fun add(postId: Int, job: Job) {
        downloads[postId] = job
    }

    override fun isPostAlreadyAdded(postId: Int): Boolean {
        return downloads.containsKey(postId)
    }

    override fun remove(id: Int) {
        downloads[id]?.cancel()
        downloads.remove(id)
    }

    override fun incrementNotificationCounter(): Int {
        notificationIdCounter += 1

        return notificationIdCounter
    }

    override fun download(
        postId: Int,
        url: String,
        sample: Long,
    ): Flow<DownloadResource> {
        return channelFlow {
            var progress = DownloadResource.Downloading()
            var lastDownloaded = 0L

            val job = downloadFile(url)
                .onEach { resource ->
                    when (resource) {
                        is DownloadResource.Downloading -> {
                            progress = resource
                        }

                        else -> send(resource)
                    }
                }
                .launchIn(CoroutineScope(Dispatchers.IO))

            add(postId, job)

            while (job.isActive) {
                send(
                    element = progress.copy(
                        speed = (progress.downloaded - lastDownloaded) / (sample / 1000L),
                    ),
                )

                lastDownloaded = progress.downloaded

                delay(timeMillis = sample)
            }
        }
    }

    private fun downloadFile(url: String): Flow<DownloadResource> {
        return flow {
            emit(DownloadResource.Starting)

            try {
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
}