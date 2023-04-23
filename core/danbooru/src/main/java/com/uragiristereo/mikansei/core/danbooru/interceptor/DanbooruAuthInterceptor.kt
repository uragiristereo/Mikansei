package com.uragiristereo.mikansei.core.danbooru.interceptor

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class DanbooruAuthInterceptor(
    name: String,
    apiKey: String,
) : Interceptor {
    private val credentials = Credentials.basic(username = name, password = apiKey)

    override fun intercept(chain: Interceptor.Chain): Response {
        val authenticatedRequest = chain.request()
            .newBuilder()
            .header(name = "Authorization", value = credentials)
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
