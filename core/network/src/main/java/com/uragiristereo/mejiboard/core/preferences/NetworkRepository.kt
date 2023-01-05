package com.uragiristereo.mejiboard.core.preferences

import com.uragiristereo.mejiboard.core.preferences.api.MejiboardApi
import okhttp3.OkHttpClient

interface NetworkRepository {
    val okHttpClient: OkHttpClient

    val api: MejiboardApi

    fun getPreferredOkHttpClient(doh: Boolean): OkHttpClient
}
