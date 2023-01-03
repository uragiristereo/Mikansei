package com.uragiristereo.mejiboard.data.network

import android.content.Context
import com.uragiristereo.mejiboard.common.util.CacheUtil
import com.uragiristereo.mejiboard.data.preferences.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    override val api: GeneralApi = Retrofit.Builder()
        .baseUrl("https://github.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GeneralApi::class.java)

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
