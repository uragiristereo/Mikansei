package com.uragiristereo.mikansei.core.model.dagger.module

import android.content.Context
import com.uragiristereo.mikansei.core.model.dagger.feature.ApplicationContext
import okhttp3.OkHttpClient

interface SingletonProvider {
    @ApplicationContext
    fun providesApplicationContext(): Context

    fun provideOkHttpClient(): OkHttpClient
}
