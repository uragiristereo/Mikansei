package com.uragiristereo.mejiboard.core.download

import com.uragiristereo.mejiboard.core.download.model.DownloadResource
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    fun incrementNotificationCounter(): Int

    fun isPostAlreadyAdded(postId: Int): Boolean

    fun remove(id: Int)

    fun download(postId: Int, url: String, sample: Long = 1000L): Flow<DownloadResource>
}
