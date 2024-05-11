package com.uragiristereo.mikansei.core.danbooru.interceptor

import com.uragiristereo.mikansei.core.danbooru.annotation.NoAuth
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

class DanbooruAuthInterceptor(
    private val userRepository: UserRepository,
) : Interceptor {
    private val activeUser: Profile
        get() = userRepository.active.value

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val invocation = request.tag(Invocation::class)
        val hasNoAuthAnnotation = invocation?.method()?.getAnnotation(NoAuth::class.java) != null
        val isUserAnonymous = activeUser.isAnonymous()

        if (hasNoAuthAnnotation || isUserAnonymous) {
            return chain.proceed(request)
        }

        val credentials = Credentials.basic(
            username = activeUser.name,
            password = activeUser.apiKey,
        )

        val authenticatedRequest = chain.request()
            .newBuilder()
            .header(name = "Authorization", value = credentials)
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
