package com.uragiristereo.mikansei.core.danbooru.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

object ForceRefreshInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val isForceRefresh = request.headers["force-refresh"]?.toBoolean() ?: false

        if (isForceRefresh) {
            val forceRefreshRequest = request
                .newBuilder()
                .removeHeader("force-refresh")
                .cacheControl(CacheControl.Builder().noCache().build())
                .build()

            return chain.proceed(forceRefreshRequest)
        }

        return chain.proceed(request)
    }
}
