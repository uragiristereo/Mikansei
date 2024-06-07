package com.uragiristereo.mikansei.core.model.dagger.module

import okhttp3.OkHttpClient

interface SingletonProvider {
    fun provideOkHttpClient(): OkHttpClient
}
