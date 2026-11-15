package com.uragiristereo.mikansei.core.danbooru.interceptor

import okhttp3.Interceptor
import okhttp3.Response

object ForceCacheRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val hasForceCache = request.headers["force-cache"] != null

        if (hasForceCache) {
            request = request.newBuilder()
                .removeHeader("force-cache")
                .build()
        }

        return chain.proceed(request)
    }
}
