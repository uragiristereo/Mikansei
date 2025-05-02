package com.uragiristereo.mikansei.core.danbooru.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class ForceLoadFromCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val isForceLoadFromCache = request.headers["force-load-from-cache"]?.toBoolean() ?: false

        if (isForceLoadFromCache) {
            val forceRefreshRequest = request
                .newBuilder()
                .removeHeader("force-load-from-cache")
                .cacheControl(
                    CacheControl.Builder()
                        .onlyIfCached()
                        .maxAge(5, timeUnit = TimeUnit.MINUTES)
                        .maxStale(5, timeUnit = TimeUnit.MINUTES)
                        .build()
                )
                .build()

            return chain.proceed(forceRefreshRequest)
        }

        return chain.proceed(request)
    }
}
