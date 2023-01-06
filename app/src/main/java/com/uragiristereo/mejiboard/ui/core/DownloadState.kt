package com.uragiristereo.mejiboard.ui.core

data class DownloadState(
    val downloaded: String = "0 B",
    val fileSize: String = "0 B",
    val progress: Float = 0f,
    val downloadSpeed: String = "0B/s",
)
