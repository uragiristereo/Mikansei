package com.uragiristereo.mejiboard.core.network

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.uragiristereo.mejiboard.core.network.api.MejiboardApi
import com.uragiristereo.mejiboard.core.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
class NetworkRepositoryImpl(
    context: Context,
    private val preferencesRepository: PreferencesRepository,
) : NetworkRepository {

    private var dohEnabled = preferencesRepository.data.dohEnabled

    private val bootstrapOkHttpClient = OkHttpClient.Builder()
        .cache(CacheUtil.createDefaultCache(context = context, path = "image_cache"))
        .addInterceptor {
            HttpLoggingInterceptor()
                .setLevel(
                    when {
//                        BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BODY
                        else -> HttpLoggingInterceptor.Level.NONE
                    }
                )
                .intercept(it)
        }
        .build()

    private val dns = DnsOverHttps.Builder()
        .client(bootstrapOkHttpClient)
        .url("https://cloudflare-dns.com/dns-query".toHttpUrl())
        .build()

    private val okHttpClientDoh = bootstrapOkHttpClient.newBuilder()
        .dns(dns)
        .build()

    override val okHttpClient
        get() = getPreferredOkHttpClient(dohEnabled)

    override val api: MejiboardApi = Retrofit.Builder()
        .baseUrl("https://github.com")
        .client(okHttpClient)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(MejiboardApi::class.java)

    init {
        CoroutineScope(Dispatchers.Main).launch {
            preferencesRepository.flowData.collect { preferences ->
                dohEnabled = preferences.dohEnabled
            }
        }
    }

    override fun getPreferredOkHttpClient(doh: Boolean): OkHttpClient {
        return when {
            doh -> okHttpClientDoh
            else -> bootstrapOkHttpClient
        }
    }
}
