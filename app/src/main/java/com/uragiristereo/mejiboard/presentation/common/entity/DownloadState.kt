package com.uragiristereo.mejiboard.presentation.common.entity

data class DownloadState(
    val downloaded: String = "0 B",
    val fileSize: String = "0 B",
    val progress: String = "0%",
    val downloadSpeed: String = "0B/s",
)
