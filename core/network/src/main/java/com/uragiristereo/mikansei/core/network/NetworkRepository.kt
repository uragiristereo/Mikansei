package com.uragiristereo.mikansei.core.network

import androidx.media3.datasource.cache.CacheDataSource
import com.uragiristereo.mikansei.core.network.api.MejiboardApi
import okhttp3.OkHttpClient

interface NetworkRepository {
    val okHttpClient: OkHttpClient
    val api: MejiboardApi
    val exoPlayerCacheFactory: CacheDataSource.Factory

    fun getPreferredOkHttpClient(doh: Boolean): OkHttpClient
}
