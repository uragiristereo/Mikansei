package com.uragiristereo.mikansei.core.network

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.okhttp.OkHttpDataSource
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.uragiristereo.mikansei.core.domain.module.network.NetworkRepository
import com.uragiristereo.mikansei.core.model.CacheUtil
import com.uragiristereo.mikansei.core.model.Environment
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.dnsoverhttps.DnsOverHttps
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import kotlin.coroutines.cancellation.CancellationException

@androidx.annotation.OptIn(UnstableApi::class)
class NetworkRepositoryImpl(
    context: Context,
    preferencesRepository: PreferencesRepository,
    environment: Environment,
) : NetworkRepository {
    private var isDohEnabled = preferencesRepository.data.value.dohEnabled

    private val bootstrapOkHttpClient = OkHttpClient.Builder()
        .apply {
            if (environment.debug) {
                addInterceptor {
                    HttpLoggingInterceptor()
//                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                        .setLevel(HttpLoggingInterceptor.Level.NONE)
                        .intercept(it)
                }
            }
        }
        .build()

    override val okHttpClient: OkHttpClient
        get() = when {
            isDohEnabled -> okHttpClientDoh
            else -> bootstrapOkHttpClient
        }

    override val okHttpClientImage by lazy {
        okHttpClient.newBuilder()
            .cache(CacheUtil.createDefaultCache(context = context, path = "image_cache"))
            .build()
    }

    private val dns = DnsOverHttps.Builder()
        .client(bootstrapOkHttpClient)
        .url("https://cloudflare-dns.com/dns-query".toHttpUrl())
        .build()

    private val okHttpClientDoh = bootstrapOkHttpClient.newBuilder()
        .dns(dns)
        .build()

    override val exoPlayerCacheFactory = CacheDataSource.Factory()
        .setCache(
            SimpleCache(
                File(context.cacheDir, "video_cache"),
                LeastRecentlyUsedCacheEvictor(1024 * 1024 * 256L),
                StandaloneDatabaseProvider(context),
            )
        )
        .setUpstreamDataSourceFactory(OkHttpDataSource.Factory(okHttpClient))

    private val client: MikanseiApi = Retrofit.Builder()
        .baseUrl("https://github.com")
        .client(okHttpClientImage)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(MikanseiApi::class.java)

    override fun getFileSize(url: String): Flow<Result<Long>> {
        return flow {
            try {
                val response = client.checkFile(url)

                if (response.isSuccessful) {
                    val size = response.headers()["content-length"]?.toLongOrNull() ?: 0L

                    emit(Result.Success(size))
                } else {
                    emit(Result.Failed(response.raw().body.toString()))
                }
            } catch (t: Throwable) {
                when (t) {
                    is CancellationException -> {}
                    else -> emit(Result.Error(t))
                }
            }
        }
    }

    override suspend fun downloadFile(url: String): ResponseBody {
        return client.downloadFile(url)
    }
}
