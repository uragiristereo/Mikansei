package com.uragiristereo.mikansei

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.uragiristereo.mikansei.core.danbooru.DanbooruModule
import com.uragiristereo.mikansei.core.database.DatabaseModule
import com.uragiristereo.mikansei.core.domain.DomainModule
import com.uragiristereo.mikansei.core.download.di.DownloadModule
import com.uragiristereo.mikansei.core.network.NetworkRepository
import com.uragiristereo.mikansei.core.network.di.NetworkModule
import com.uragiristereo.mikansei.core.preferences.PreferencesModule
import com.uragiristereo.mikansei.feature.image.ImageModule
import com.uragiristereo.mikansei.feature.user.UserModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import timber.log.Timber

class MikanseiApp : Application(), ImageLoaderFactory, KoinComponent {
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
            androidContext(this@MikanseiApp)
            modules(
                listOf(
                    MikanseiAppModule(),
                    DanbooruModule(),
                    DatabaseModule(),
                    DomainModule(),
                    DownloadModule(),
                    NetworkModule(),
                    PreferencesModule(),
                    ImageModule(),
                    UserModule(),
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
