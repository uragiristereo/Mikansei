package com.uragiristereo.mikansei.core.danbooru.interceptor

import com.uragiristereo.mikansei.core.danbooru.annotation.NoSafeHost
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.Environment
import com.uragiristereo.mikansei.core.model.danbooru.DanbooruHost
import com.uragiristereo.mikansei.core.model.preferences.user.RatingPreference
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

class DanbooruHostInterceptor(
    private val userRepository: UserRepository,
    private val preferencesRepository: PreferencesRepository,
    private val environment: Environment,
) : Interceptor {
    private val isInTestMode: Boolean
        get() = preferencesRepository.data.value.testMode

    val host: DanbooruHost
        get() = when {
            isInTestMode -> DanbooruHost.Testbooru
            isInSafeMode -> DanbooruHost.Safebooru
            else -> DanbooruHost.Danbooru
        }

    private val isInSafeMode: Boolean
        get() {
            val activeUser = userRepository.active.value
            return environment.safeMode || activeUser.danbooru.safeMode || activeUser.mikansei.postsRatingFilter == RatingPreference.GENERAL_ONLY
        }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val invocation = request.tag(Invocation::class)

        val hasNoSafeHostAnnotation = invocation?.method()
            ?.getAnnotation(NoSafeHost::class.java) != null

        val preferredHost = when {
            hasNoSafeHostAnnotation && isInTestMode -> DanbooruHost.Testbooru
            hasNoSafeHostAnnotation -> DanbooruHost.Danbooru
            else -> host
        }

        val newUrl = request.url.newBuilder()
            .host(preferredHost.getBaseUrl().toHttpUrl().host)
            .build()

        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
