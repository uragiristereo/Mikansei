package com.uragiristereo.mejiboard

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.uragiristereo.mejiboard.di.AppModule
import com.uragiristereo.mejiboard.di.DatabaseModule
import com.uragiristereo.mejiboard.di.NetworkModule
import com.uragiristereo.mejiboard.domain.repository.NetworkRepository
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import timber.log.Timber

class MainApplication : Application(), ImageLoaderFactory, KoinComponent {
    private val networkRepository: NetworkRepository by inject()

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                listOf(
                    AppModule.module,
                    NetworkModule.module,
                    DatabaseModule.module,
                )
            )
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(context = this)
            .diskCache(null)
            .respectCacheHeaders(enable = false)
            .okHttpClient { networkRepository.okHttpClient }
            .build()
    }
}
