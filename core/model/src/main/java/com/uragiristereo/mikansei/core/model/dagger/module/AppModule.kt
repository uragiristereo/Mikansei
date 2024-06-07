package com.uragiristereo.mikansei.core.model.dagger.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
object AppModule {
    @Singleton
    @Provides
    fun providesOkhttpClient(): OkHttpClient {
        return OkHttpClient()
    }
}
