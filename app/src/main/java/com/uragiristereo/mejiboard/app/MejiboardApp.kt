package com.uragiristereo.mejiboard.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.uragiristereo.mejiboard.app.di.AppModule
import com.uragiristereo.mejiboard.core.booru.di.BooruModule
import com.uragiristereo.mejiboard.core.database.di.DatabaseModule
import com.uragiristereo.mejiboard.core.download.di.DownloadModule
import com.uragiristereo.mejiboard.core.network.NetworkRepository
import com.uragiristereo.mejiboard.core.network.di.NetworkModule
import com.uragiristereo.mejiboard.core.preferences.di.PreferencesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import timber.log.Timber

class MejiboardApp : Application(), ImageLoaderFactory, KoinComponent {
    private val networkRepository: NetworkRepository by inject()

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        if (SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("downloads", "Downloads", NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        startKoin {
            androidLogger()
            androidContext(this@MejiboardApp)
            modules(
                listOf(
                    AppModule(),
                    NetworkModule(),
                    DatabaseModule(),
                    BooruModule(),
                    DownloadModule(),
                    PreferencesModule(),
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
