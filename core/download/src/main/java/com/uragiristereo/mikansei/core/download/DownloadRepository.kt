package com.uragiristereo.mikansei.core.download

import android.net.Uri
import com.uragiristereo.mikansei.core.download.model.DownloadResource
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    fun incrementNotificationCounter(): Int

    fun isPostAlreadyAdded(postId: Int): Boolean

    fun remove(id: Int)

    fun download(postId: Int, url: String, uri: Uri, sample: Long = 1000L): Flow<DownloadResource>
}
