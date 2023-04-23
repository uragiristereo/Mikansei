package com.uragiristereo.mikansei.core.danbooru.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class ForceCacheResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        val forceCache = response.request.headers["force-cache"]?.toBoolean() ?: false

        if (forceCache) {
            val cacheControl = CacheControl.Builder()
                .maxAge(10, TimeUnit.MINUTES)
                .build()

            return response.newBuilder()
                .removeHeader("cache-control")
                .header("cache-control", cacheControl.toString())
                .build()
        }

        return response
    }
}
