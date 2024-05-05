package com.uragiristereo.mikansei.core.domain.module.network

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.CacheDataSource
import com.uragiristereo.mikansei.core.model.result.Result
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.ResponseBody

@OptIn(UnstableApi::class)
interface NetworkRepository {
    val okHttpClient: OkHttpClient
    val exoPlayerCacheFactory: CacheDataSource.Factory

    fun getFileSize(url: String): Flow<Result<Long>>

    suspend fun downloadFile(url: String): ResponseBody
}
