package com.uragiristereo.mejiboard.data.network

import okhttp3.OkHttpClient

interface NetworkRepository {
    val okHttpClient: OkHttpClient

    val api: GeneralApi

    fun getPreferredOkHttpClient(doh: Boolean): OkHttpClient
}
