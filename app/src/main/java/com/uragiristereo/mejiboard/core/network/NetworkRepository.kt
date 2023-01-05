package com.uragiristereo.mejiboard.core.network

import com.uragiristereo.mejiboard.core.network.api.MejiboardApi
import okhttp3.OkHttpClient

interface NetworkRepository {
    val okHttpClient: OkHttpClient

    val api: MejiboardApi

    fun getPreferredOkHttpClient(doh: Boolean): OkHttpClient
}
