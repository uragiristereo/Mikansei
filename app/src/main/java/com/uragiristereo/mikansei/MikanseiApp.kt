package com.uragiristereo.mikansei

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.uragiristereo.mikansei.core.domain.module.network.NetworkRepository
import com.uragiristereo.mikansei.core.model.dagger.component.AppComponent
import com.uragiristereo.mikansei.core.model.dagger.component.AppComponentProvider
import com.uragiristereo.mikansei.core.model.dagger.component.DaggerAppComponent
import com.uragiristereo.mikansei.di.DaggerMikanseiAppComponent
import com.uragiristereo.serializednavigationextension.runtime.installSerializer
import com.uragiristereo.serializednavigationextension.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import timber.log.Timber
import javax.inject.Inject

class MikanseiApp : Application(), ImageLoaderFactory, KoinComponent, AppComponentProvider {
    private val networkRepository: NetworkRepository by inject()
    private lateinit var appComponent: AppComponent

    @Inject
    lateinit var okHttpClient: OkHttpClient

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        installSerializer(KotlinxSerializer(json = Json { ignoreUnknownKeys = true }))

        if (SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("downloads", "Downloads", NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        startKoin {
            androidLogger()
            androidContext(this@MikanseiApp)
            modules(MikanseiModule())
        }

        appComponent = DaggerAppComponent.builder()
            .applicationContext(this)
            .build()

        DaggerMikanseiAppComponent.factory()
            .create(appComponent)
            .inject(this)

        Timber.d(okHttpClient.toString())
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(context = this)
            .diskCache(null)
            .respectCacheHeaders(enable = false)
            .okHttpClient(networkRepository.okHttpClientImage)
            .build()
    }

    override fun getComponent(): AppComponent {
        return appComponent
    }
}
