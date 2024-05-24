package com.uragiristereo.mikansei.core.danbooru.interceptor

import com.uragiristereo.mikansei.core.domain.module.database.UserDelegationRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class UserDelegationInterceptor(
    private val userDelegationRepository: UserDelegationRepository,
    private val userRepository: UserRepository,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath
        val tags = request.url.queryParameter("tags")
        val isPathGetPosts = path.contains("/posts")
        val areTagsContainsPrivateTag = when {
            tags != null -> privateTags.any { tags.contains(it) }
            else -> false
        }

        if (isPathGetPosts && !areTagsContainsPrivateTag) {
            val activeUserId = userRepository.active.value.id
            val delegatedUserId = runBlocking {
                userDelegationRepository.getDelegatedUserById(activeUserId).first()
            }

            if (delegatedUserId != null) {
                if (delegatedUserId != activeUserId) {
                    val delegatedUser = runBlocking {
                        userRepository.getOnce(delegatedUserId)
                    }

                    if (delegatedUser != null) {
                        val delegatedCredentials = Credentials.basic(
                            username = delegatedUser.name,
                            password = delegatedUser.apiKey
                        )

                        val newRequest = request.newBuilder()
                            .removeHeader(name = "Authorization")
                            .addHeader(name = "Authorization", value = delegatedCredentials)
                            .build()

                        Timber.d("search delegated to user = ${delegatedUser.name}")

                        return chain.proceed(newRequest)
                    } else {
                        runBlocking {
                            userDelegationRepository.removeDelegation(activeUserId)
                        }
                    }
                } else {
                    runBlocking {
                        userDelegationRepository.removeDelegation(activeUserId)
                    }
                }
            }
        }

        return chain.proceed(request)
    }

    companion object {
        private val privateTags = listOf(
            "search:",
            "favgroup:",
            "ordfav:"
        )
    }
}
