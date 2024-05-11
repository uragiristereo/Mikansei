package com.uragiristereo.mikansei.core.danbooru.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

object ForceCacheResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val hasForceCache = request.headers["force-cache"]?.toBoolean() ?: false

        if (hasForceCache) {
            request = request.newBuilder()
                .removeHeader("force-cache")
                .build()
        }

        val response = chain.proceed(request)

        if (hasForceCache) {
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
