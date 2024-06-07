package com.uragiristereo.mikansei.core.model.dagger.module

import android.app.Application
import android.content.Context
import com.uragiristereo.mikansei.core.model.dagger.feature.ApplicationContext
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class AppModule {
    @ApplicationContext
    @Singleton
    @Provides
    fun providesApplicationContext(application: Application): Context {
        return application
    }

    @Singleton
    @Provides
    fun providesOkhttpClient(): OkHttpClient {
        return OkHttpClient()
    }
}
