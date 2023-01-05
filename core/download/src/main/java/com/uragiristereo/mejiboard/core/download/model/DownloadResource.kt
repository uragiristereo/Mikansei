package com.uragiristereo.mejiboard.core.download.model

sealed class DownloadResource {
    object Starting : DownloadResource()

    data class Downloading(
        val downloaded: Long = 0L,
        val length: Long = 0L,
        val progress: Float = 0f,
    ) : DownloadResource()

    data class Completed(val length: Long) : DownloadResource()

    data class Failed(val t: Throwable?) : DownloadResource()
}
