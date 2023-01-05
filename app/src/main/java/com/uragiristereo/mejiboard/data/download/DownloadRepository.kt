package com.uragiristereo.mejiboard.data.download

import com.uragiristereo.mejiboard.data.download.model.DownloadResource
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    suspend fun add(postId: Int, url: String)

    fun isPostAlreadyAdded(postId: Int): Boolean

    fun remove(id: Int)

    fun download(url: String): Flow<DownloadResource>
}
