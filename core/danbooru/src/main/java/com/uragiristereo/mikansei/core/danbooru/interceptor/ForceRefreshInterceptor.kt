package com.uragiristereo.mikansei.core.danbooru.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

object ForceRefreshInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val isForceRefresh = request.headers["force-cache"]?.toBoolean() ?: false

        if (isForceRefresh) {
            val forceRefreshRequest = request
                .newBuilder()
                .removeHeader("force-cache")
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build()

            return chain.proceed(forceRefreshRequest)
        }

        return chain.proceed(request)
    }
}
