package com.uragiristereo.mikansei.core.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.uragiristereo.mikansei.core.domain.module.network.DownloadRepository
import com.uragiristereo.mikansei.core.domain.module.network.NetworkRepository
import com.uragiristereo.mikansei.core.domain.module.network.entity.DownloadResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@SuppressLint("MissingPermission")
class DownloadRepositoryImpl(
    context: Context,
    private val networkRepository: NetworkRepository,
) : DownloadRepository {
    private val downloads: MutableMap<Int, Job> = mutableMapOf()
    private var notificationIdCounter = 0

    private val resolver = context.contentResolver

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
        uri: Uri,
        sample: Long,
    ): Flow<DownloadResource> {
        return channelFlow {
            var progress = DownloadResource.Downloading()
            var lastDownloaded = 0L

            val job = downloadFile(url, uri)
                .onEach { resource ->
                    when (resource) {
                        DownloadResource.Starting -> {
                            send(resource)
                        }

                        is DownloadResource.Downloading -> {
                            progress = resource
                        }

                        is DownloadResource.Completed, is DownloadResource.Failed -> {
                            remove(postId)
                            send(resource)
                        }
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

    private fun downloadFile(
        url: String,
        uri: Uri,
    ): Flow<DownloadResource> {
        return flow {
            emit(DownloadResource.Starting)

            try {
                var length: Long

                val result = networkRepository.downloadFile(url)

                result.byteStream().use { inputStream ->
                    resolver.openOutputStream(uri)!!.use { outputStream ->
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
