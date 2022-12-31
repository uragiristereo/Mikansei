package com.uragiristereo.mejiboard.domain.repository

import com.uragiristereo.mejiboard.data.network.GeneralApi
import okhttp3.OkHttpClient

interface NetworkRepository {
    val okHttpClient: OkHttpClient

    val api: GeneralApi

    fun getPreferredOkHttpClient(doh: Boolean): OkHttpClient
}
