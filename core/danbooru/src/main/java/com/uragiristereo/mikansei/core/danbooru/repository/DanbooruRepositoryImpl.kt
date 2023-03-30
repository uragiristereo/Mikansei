package com.uragiristereo.mikansei.core.danbooru.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.uragiristereo.mikansei.core.danbooru.DanbooruApi
import com.uragiristereo.mikansei.core.danbooru.DanbooruAuthInterceptor
import com.uragiristereo.mikansei.core.danbooru.model.post.DanbooruPost
import com.uragiristereo.mikansei.core.danbooru.model.user.DanbooruUser
import com.uragiristereo.mikansei.core.danbooru.model.user.field.DanbooruUserField
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.database.dao.user.toUser
import com.uragiristereo.mikansei.core.model.danbooru.DanbooruHost
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.failedResponseFormatter
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import com.uragiristereo.mikansei.core.model.result.resultFlow
import com.uragiristereo.mikansei.core.model.user.User
import com.uragiristereo.mikansei.core.network.NetworkRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
open class DanbooruRepositoryImpl(
    private val networkRepository: NetworkRepository,
    private val userDao: UserDao,
) : DanbooruRepository {
    open val host = DanbooruHost.Danbooru

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private val activeUser = runBlocking {
        userDao.getActive().first().toUser()
    }

    private var client = buildClient(user = activeUser, auth = true)
    private val clientNoAuth = buildClient(auth = false)

    init {
        CoroutineScope(context = Dispatchers.IO + SupervisorJob()).launch {
            userDao.getActive().collect { userRow ->
                client = buildClient(user = userRow.toUser(), auth = true)
            }
        }
    }

    private fun buildClient(user: User? = null, auth: Boolean): DanbooruApi {
        val client = networkRepository.okHttpClient
            .newBuilder()
            .let { builder ->
                if (user != null && user.apiKey.isNotEmpty() && auth) {
                    builder.addInterceptor(
                        DanbooruAuthInterceptor(user.name, user.apiKey)
                    )
                }

                builder
            }.build()

        return Retrofit.Builder()
            .baseUrl(host.getBaseUrl())
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(DanbooruApi::class.java)
    }

    override suspend fun getPost(id: Int): Flow<Result<DanbooruPost>> = resultFlow {
        client.getPost(id)
    }

    override suspend fun getPosts(tags: String, page: Int) = resultFlow {
        client.getPosts(tags, page)
    }

    override suspend fun getTagsAutoComplete(query: String) = resultFlow {
        client.getTagsAutoComplete(query)
    }

    override suspend fun getTags(tags: List<String>) = resultFlow {
        client.getTags(tags)
    }

    override suspend fun getProfile() = resultFlow {
        client.getProfile()
    }

    override suspend fun isPostInFavorites(postId: Int, userId: Int): Flow<Result<Boolean>> = resultFlow {
        client.getFavorites(postId, userId)
    }.mapSuccess { favorites ->
        favorites.any { it.postId == postId && it.userId == userId }
    }

    override suspend fun login(name: String, apiKey: String) = resultFlow {
        clientNoAuth.getProfile(
            credentials = Credentials.basic(
                username = name,
                password = apiKey,
            )
        )
    }

    override suspend fun getUser(userId: Int) = resultFlow {
        client.getUser(userId)
    }

    override suspend fun getUsers(userIds: List<Int>): Flow<Result<Map<Int, DanbooruUser>>> = flow {
        val users: MutableMap<Int, DanbooruUser> = mutableMapOf()

        try {
            userIds.forEach { id ->
                val response = client.getUser(id)

                when {
                    response.isSuccessful -> {
                        val data = response.body()!!

                        users[id] = data
                    }

                    else -> {
                        emit(
                            Result.Failed(
                                message = failedResponseFormatter(
                                    responseCode = response.code(),
                                    errorBody = response.errorBody()?.string(),
                                )
                            )
                        )

                        return@flow
                    }
                }
            }

            emit(Result.Success(users))
        } catch (t: Throwable) {
            when (t) {
                is CancellationException -> {}
                else -> emit(Result.Error(t))
            }
        }
    }

    override suspend fun updateUserSettings(id: Int, data: DanbooruUserField) = resultFlow {
        client.updateUserSettings(id, data)
    }
}



