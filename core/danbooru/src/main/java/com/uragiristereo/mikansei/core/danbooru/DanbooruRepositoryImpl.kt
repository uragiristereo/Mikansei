package com.uragiristereo.mikansei.core.danbooru

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.uragiristereo.mikansei.core.danbooru.model.favorite.toFavorite
import com.uragiristereo.mikansei.core.danbooru.model.favorite.toFavoriteList
import com.uragiristereo.mikansei.core.danbooru.model.post.toPost
import com.uragiristereo.mikansei.core.danbooru.model.post.toPostList
import com.uragiristereo.mikansei.core.danbooru.model.post.toPostVote
import com.uragiristereo.mikansei.core.danbooru.model.profile.toProfile
import com.uragiristereo.mikansei.core.danbooru.model.tag.toTagList
import com.uragiristereo.mikansei.core.danbooru.model.user.field.DanbooruUserField
import com.uragiristereo.mikansei.core.danbooru.model.user.field.DanbooruUserFieldData
import com.uragiristereo.mikansei.core.danbooru.model.user.toUser
import com.uragiristereo.mikansei.core.danbooru.retrofit.DanbooruApi
import com.uragiristereo.mikansei.core.danbooru.retrofit.DanbooruAuthInterceptor
import com.uragiristereo.mikansei.core.danbooru.retrofit.ForceCacheResponseInterceptor
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostVote
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostsResult
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.ProfileSettingsField
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.User
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.domain.module.network.NetworkRepository
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.danbooru.DanbooruHost
import com.uragiristereo.mikansei.core.model.preferences.user.DetailSizePreference
import com.uragiristereo.mikansei.core.model.preferences.user.RatingPreference
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import com.uragiristereo.mikansei.core.model.result.resultFlow
import com.uragiristereo.mikansei.core.resources.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private val userRepository: UserRepository,
) : DanbooruRepository {
    override val isProd = true
    override var unsafeTags: List<String> = listOf()

    open val host = DanbooruHost.Danbooru
    open val safeHost = DanbooruHost.Safebooru

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private var activeUser = userRepository.active.value

    private lateinit var clientAuth: DanbooruApi
    private lateinit var clientNoAuth: DanbooruApi
    private lateinit var clientSafe: DanbooruApi

    private val client: DanbooruApi
        get() = when {
            activeUser.danbooru.safeMode || activeUser.mikansei.postsRatingFilter == RatingPreference.GENERAL_ONLY -> clientSafe
            else -> clientAuth
        }

    init {
        buildClients(activeUser)

        CoroutineScope(context = Dispatchers.IO + SupervisorJob()).launch {
            userRepository.active.collect { user ->
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

    private fun buildClients(profile: Profile) {
        val okHttpClient = networkRepository.okHttpClient

        val okHttpClientWithAuth = okHttpClient
            .newBuilder()
            .addInterceptor(
                DanbooruAuthInterceptor(profile.name, profile.apiKey)
            )
            .addNetworkInterceptor(ForceCacheResponseInterceptor())
            .build()

        val preferredOkHttpClient = when {
            profile.id != 0 -> okHttpClientWithAuth
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

    override fun getPost(id: Int) = resultFlow {
        clientAuth.getPost(id)
    }.mapSuccess {
        it.toPost()
    }

    override fun getPosts(tags: String, page: Int) = resultFlow {
        client.getPosts(tags, page)
    }.mapSuccess {
        PostsResult(
            posts = it.toPostList(),
            canLoadMore = it.size == Constants.POSTS_PER_PAGE,
        )
    }

    override fun getTagsAutoComplete(query: String) = resultFlow {
        client.getTagsAutoComplete(query)
    }.mapSuccess {
        it.toTagList()
    }

    override fun getTags(tags: List<String>) = resultFlow {
        client.getTags(tags)
    }.mapSuccess {
        it.toTagList()
    }

    override fun isPostInFavorites(postId: Int, userId: Int) = resultFlow {
        client.getFavorites(postId, userId)
    }.mapSuccess { favorites ->
        favorites.any { it.postId == postId && it.userId == userId }
    }

    override fun getProfile() = resultFlow {
        client.getProfile()
    }.mapSuccess {
        it.toProfile()
    }

    override fun login(name: String, apiKey: String) = resultFlow {
        clientNoAuth.getProfile(
            credentials = Credentials.basic(
                username = name,
                password = apiKey,
            )
        )
    }.mapSuccess {
        it.toProfile()
    }

    override fun getUser(id: Int) = resultFlow {
        client.getUser(id)
    }.mapSuccess {
        it.toUser()
    }

    override fun getUsers(ids: List<Int>): Flow<Result<Map<Int, User>>> {
        TODO("Not yet implemented")
    }

    override fun updateUserSettings(id: Int, field: ProfileSettingsField) = resultFlow {
        client.updateUserSettings(
            id = id,
            data = DanbooruUserField(
                user = DanbooruUserFieldData(
                    enableSafeMode = field.enableSafeMode,
                    showDeletedPosts = field.showDeletedPosts,
                    defaultImageSize = when (field.defaultImageSize) {
                        DetailSizePreference.COMPRESSED -> "large"
                        DetailSizePreference.ORIGINAL -> "original"
                        null -> null
                    },
                    blacklistedTags = field.blacklistedTags?.joinToString("\n"),
                )
            )
        )
    }

    override fun getFavoriteGroups(creatorId: Int, forceRefresh: Boolean) = resultFlow {
        client.getFavoriteGroups(creatorId, getCacheControl(forceRefresh))
    }.mapSuccess { favoriteGroups ->
        favoriteGroups.sortedByDescending { it.updatedAt }.toFavoriteList()
    }

    override fun getPostsByIds(ids: List<Int>, forceCache: Boolean, forceRefresh: Boolean) = resultFlow {
        val separated = ids.joinToString(separator = ",")

        client.getPosts(
            tags = "id:$separated",
            pageId = 1,
            forceCache = forceCache,
            cacheControl = getCacheControl(forceRefresh).toString(),
        )
    }.mapSuccess {
        it.toPostList()
    }

    override fun addToFavorites(postId: Int) = resultFlow {
        client.addToFavorites(postId)
    }

    override fun deleteFromFavorites(postId: Int) = resultFlow {
        client.deleteFromFavorites(postId)
    }

    override fun getPostVote(postId: Int, userId: Int) = resultFlow {
        client.getPostVotes(postId, userId)
    }.mapSuccess { postVotes ->
        postVotes.toPostVote(postId, userId)
    }

    override fun votePost(postId: Int, score: PostVote.Status) = resultFlow {
        when {
            score == PostVote.Status.NONE -> client.unvotePost(postId)

            else -> client.votePost(
                postId = postId,
                score = when (score) {
                    PostVote.Status.UPVOTED -> 1
                    PostVote.Status.DOWNVOTED -> -1
                    else -> 0
                },
            )
        }
    }

    override fun addPostToFavoriteGroup(favoriteGroupId: Int, postId: Int) = resultFlow {
        client.addPostToFavoriteGroup(favoriteGroupId, postId)
    }

    override fun removePostFromFavoriteGroup(favoriteGroupId: Int, postId: Int) = resultFlow {
        client.removePostFromFavoriteGroup(favoriteGroupId, postId)
    }

    override fun createNewFavoriteGroup(name: String, postIds: List<Int>) = resultFlow {
        client.createNewFavoriteGroup(name, postIds.joinToString(separator = " "))
    }.mapSuccess {
        it.toFavorite()
    }
}
