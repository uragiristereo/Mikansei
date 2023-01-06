package com.uragiristereo.mejiboard.core.download.model

import androidx.core.app.NotificationCompat

data class DownloadInfo(
    val postId: Int,
    val url: String,
    val notificationId: Int,
    val notificationBuilder: NotificationCompat.Builder,
)
