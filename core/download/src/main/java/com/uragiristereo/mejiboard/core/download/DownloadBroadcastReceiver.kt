package com.uragiristereo.mejiboard.core.download

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DownloadBroadcastReceiver : BroadcastReceiver(), KoinComponent {
    private val downloadRepository: DownloadRepository by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        val postId = intent?.action?.toInt()
        val notificationId = intent?.extras?.getInt(/* key = */ "notificationId", /* defaultValue = */ -1)
        val notificationManager = context?.let { NotificationManagerCompat.from(it) }

        notificationId?.let { notificationManager?.cancel(it) }
        postId?.let { downloadRepository.remove(it) }
    }
}
