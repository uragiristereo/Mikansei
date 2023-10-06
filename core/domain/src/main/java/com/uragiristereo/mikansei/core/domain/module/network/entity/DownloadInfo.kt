package com.uragiristereo.mikansei.core.domain.module.network.entity

import androidx.core.app.NotificationCompat

data class DownloadInfo(
    val postId: Int,
    val url: String,
    val notificationId: Int,
    val notificationBuilder: NotificationCompat.Builder,
)
