package com.uragiristereo.mikansei.core.danbooru.repository

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.uragiristereo.mikansei.core.danbooru.DanbooruApi
import com.uragiristereo.mikansei.core.danbooru.interceptor.DanbooruAuthInterceptor
import com.uragiristereo.mikansei.core.danbooru.interceptor.ForceCacheResponseInterceptor
import com.uragiristereo.mikansei.core.danbooru.model.post.DanbooruPost
import com.uragiristereo.mikansei.core.danbooru.model.post.vote.DanbooruPostVote
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
import com.uragiristereo.mikansei.core.model.user.preference.RatingPreference
import com.uragiristereo.mikansei.core.network.NetworkRepository
import com.uragiristereo.mikansei.core.resources.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.CacheControl
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.zip.GZIPInputStream

@OptIn(ExperimentalSerializationApi::class)
open class DanbooruRepositoryImpl(
    private val context: Context,
    private val networkRepository: NetworkRepository,
    private val userDao: UserDao,
) : DanbooruRepository {
    open val host = DanbooruHost.Danbooru
    open val safeHost = DanbooruHost.Safebooru

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private var activeUser = runBlocking {
        userDao.getActive().first().toUser()
    }

    private lateinit var clientAuth: DanbooruApi
    private lateinit var clientNoAuth: DanbooruApi
    private lateinit var clientSafe: DanbooruApi

    private val client: DanbooruApi
        get() = when {
            activeUser.safeMode || activeUser.postsRatingFilter == RatingPreference.GENERAL_ONLY -> clientSafe
            else -> clientAuth
        }

    override var unsafeTags: List<String> = listOf()

    init {
        buildClients(activeUser)

        CoroutineScope(context = Dispatchers.IO + SupervisorJob()).launch {
            userDao.getActive().collect { userRow ->
                val user = userRow.toUser()

                if (activeUser.id != user.id) {
                    buildClients(user)
                }

                activeUser = user
            }
        }

        loadUnsafeTags()
    }

    private fun loadUnsafeTags() {
        CoroutineScope(context = Dispatchers.Default).launch {
            val rawResource = context.resources.openRawResource(R.raw.unsafe_tags)

            val stream = withContext(Dispatchers.IO) {
                GZIPInputStream(rawResource)
            }

            val text = stream.bufferedReader().use { it.readText() }

            unsafeTags = text.split('\n')
        }
    }

    private fun buildClients(user: User) {
        val okHttpClient = networkRepository.okHttpClient

        val okHttpClientWithAuth = okHttpClient
            .newBuilder()
            .addInterceptor(
                DanbooruAuthInterceptor(user.name, user.apiKey)
            )
            .addNetworkInterceptor(ForceCacheResponseInterceptor())
            .build()

        val preferredOkHttpClient = when {
            user.id != 0 -> okHttpClientWithAuth
            else -> okHttpClient
        }

        clientAuth = buildClient(preferredOkHttpClient, host)
        clientNoAuth = buildClient(okHttpClient, host)
        clientSafe = buildClient(preferredOkHttpClient, safeHost)
    }

    private fun buildClient(
        okHttpClient: OkHttpClient,
        host: DanbooruHost,
    ): DanbooruApi {
        return Retrofit.Builder()
            .baseUrl(host.getBaseUrl())
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(DanbooruApi::class.java)
    }

    private fun getCacheControl(forceRefresh: Boolean): CacheControl {
        return CacheControl.Builder()
            .let { builder ->
                when {
                    forceRefresh -> builder.noCache()
                    else -> builder
                }
            }
            .build()
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

    override suspend fun getFavoriteGroups(creatorId: Int, forceRefresh: Boolean) = resultFlow {
        client.getFavoriteGroups(creatorId, getCacheControl(forceRefresh))
    }

    override suspend fun getPostsByIds(ids: List<Int>, forceCache: Boolean, forceRefresh: Boolean) = resultFlow {
        val separated = ids.joinToString(separator = ",")

        client.getPosts(
            tags = "id:$separated",
            pageId = 1,
            forceCache = forceCache,
            cacheControl = getCacheControl(forceRefresh).toString(),
        )
    }

    override suspend fun addToFavorites(postId: Int) = resultFlow {
        client.addToFavorites(postId)
    }

    override suspend fun deleteFromFavorites(postId: Int) = resultFlow {
        client.deleteFromFavorites(postId)
    }

    override suspend fun getPostVote(postId: Int, userId: Int): Flow<Result<DanbooruPostVote?>> = resultFlow {
        client.getPostVotes(postId, userId)
    }.mapSuccess { postVotes ->
        postVotes.firstOrNull { it.postId == postId && it.userId == userId }
    }

    override suspend fun votePost(postId: Int, score: Int) = resultFlow {
        client.votePost(postId, score)
    }

    override suspend fun unvotePost(postId: Int) = resultFlow {
        client.unvotePost(postId)
    }

    override suspend fun addPostToFavoriteGroup(favoriteGroupId: Int, postId: Int) = resultFlow {
        client.addPostToFavoriteGroup(favoriteGroupId, postId)
    }
}



