package com.uragiristereo.mejiboard.data.download

interface DownloadRepository {
    suspend fun add(postId: Int, url: String)

    fun isPostAlreadyAdded(postId: Int): Boolean

    fun remove(id: Int)
}
