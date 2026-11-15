package com.uragiristereo.mikansei.core.danbooru.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

object ForceRefreshInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val hasForceRefresh = request.headers["force-refresh"] != null
        val isForceRefresh = request.headers["force-refresh"]?.toBoolean() ?: false

        if (hasForceRefresh) {
            request = request.newBuilder()
                .removeHeader("force-refresh")
                .build()
        }

        if (isForceRefresh) {
            request = request
                .newBuilder()
                .cacheControl(CacheControl.Builder().noCache().build())
                .build()

            return chain.proceed(request)
        }

        return chain.proceed(request)
    }
}
