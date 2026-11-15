package com.uragiristereo.mikansei.core.danbooru.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import retrofit2.http.Header
import java.util.concurrent.TimeUnit

object ForceCacheResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val invocation = request.tag(Invocation::class)
        val isForceCache = invocation?.getHeaderArgument("force-cache") == true

        val response = chain.proceed(request)

        if (isForceCache) {
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

    private fun Invocation.getHeaderArgument(name: String): Any? {
        val index = method().parameterAnnotations.indexOfFirst { annotations ->
            annotations.any { it is Header && it.value == name }
        }

        return arguments().getOrNull(index)
    }
}
