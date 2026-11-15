package com.uragiristereo.mikansei.core.danbooru.interceptor

import android.content.Context
import com.uragiristereo.mikansei.core.danbooru.annotation.NoAuth
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

class DanbooruUserAgentInterceptor(
    private val context: Context,
    private val userRepository: UserRepository,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val invocation = request.tag(Invocation::class)
        val hasNoAuthAnnotation = invocation?.method()?.getAnnotation(NoAuth::class.java) != null
        val packageName = context.packageName
        val version = context.packageManager.getPackageInfo(packageName, 0).versionName
        val activeUser = userRepository.active.value
        val delegatedUserId = request.header("delegated-to")
        val activeUserId = when {
            hasNoAuthAnnotation -> "$MIKANSEI_DEFAULT_USER_ID (anon)"
            delegatedUserId != null -> delegatedUserId
            activeUser.isNotAnonymous() -> "${activeUser.id}"
            else -> "$MIKANSEI_DEFAULT_USER_ID (anon)"
        }

        val newRequest = request.newBuilder()
            .removeHeader("delegated-to")
            .header("User-Agent", "$packageName/$version; user #$activeUserId")
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val MIKANSEI_DEFAULT_USER_ID = 1175330
    }
}
