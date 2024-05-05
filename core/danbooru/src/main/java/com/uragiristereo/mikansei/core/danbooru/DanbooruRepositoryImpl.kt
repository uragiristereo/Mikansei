package com.uragiristereo.mikansei.core.danbooru

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.uragiristereo.mikansei.core.danbooru.model.favorite.toFavorite
import com.uragiristereo.mikansei.core.danbooru.model.favorite.toFavoriteList
import com.uragiristereo.mikansei.core.danbooru.model.post.DanbooruPost
import com.uragiristereo.mikansei.core.danbooru.model.post.toPost
import com.uragiristereo.mikansei.core.danbooru.model.post.toPostList
import com.uragiristereo.mikansei.core.danbooru.model.post.toPostVote
import com.uragiristereo.mikansei.core.danbooru.model.profile.toProfile
import com.uragiristereo.mikansei.core.danbooru.model.saved_search.toSavedSearch
import com.uragiristereo.mikansei.core.danbooru.model.saved_search.toSavedSearchList
import com.uragiristereo.mikansei.core.danbooru.model.tag.toTagList
import com.uragiristereo.mikansei.core.danbooru.model.user.field.DanbooruUserField
import com.uragiristereo.mikansei.core.danbooru.model.user.field.DanbooruUserFieldData
import com.uragiristereo.mikansei.core.danbooru.model.user.toUser
import com.uragiristereo.mikansei.core.danbooru.retrofit.DanbooruApi
import com.uragiristereo.mikansei.core.danbooru.retrofit.DanbooruAuthInterceptor
import com.uragiristereo.mikansei.core.danbooru.retrofit.ForceCacheResponseInterceptor
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostVote
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostsResult
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.ProfileSettingsField
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.SavedSearch
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.User
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.domain.module.network.NetworkRepository
import com.uragiristereo.mikansei.core.model.CacheUtil
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.Environment
import com.uragiristereo.mikansei.core.model.danbooru.DanbooruHost
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.preferences.user.DetailSizePreference
import com.uragiristereo.mikansei.core.model.preferences.user.RatingPreference
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import com.uragiristereo.mikansei.core.model.result.resultOf
import com.uragiristereo.mikansei.core.preferences.PreferencesRepository
import com.uragiristereo.mikansei.core.resources.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
class DanbooruRepositoryImpl(
    private val context: Context,
    private val networkRepository: NetworkRepository,
    private val userRepository: UserRepository,
    preferencesRepository: PreferencesRepository,
    private val environment: Environment,
) : DanbooruRepository {
    private val isInTestMode = preferencesRepository.data.value.testMode
    override var unsafeTags: List<String> = listOf()

    private val cacheClearedEndpoints = listOf(
        "/saved_searches.json",
        "/favorite_groups.json",
    )

    private val cache = CacheUtil.createDefaultCache(context = context, path = "http_cache")
    private var activeUser = userRepository.active.value

    private val actualHost = when {
        isInTestMode -> DanbooruHost.Testbooru
        else -> DanbooruHost.Danbooru
    }

    private val safeHost = when {
        isInTestMode -> DanbooruHost.Testbooru
        else -> DanbooruHost.Safebooru
    }

    override val host: DanbooruHost
        get() = when {
            isInSafeMode() -> safeHost
            else -> actualHost
        }

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private lateinit var clientAuth: DanbooruApi
    private lateinit var clientNoAuth: DanbooruApi
    private lateinit var clientSafe: DanbooruApi

    private val client: DanbooruApi
        get() = when {
            isInSafeMode() -> clientSafe
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
            .cache(cache)
            .addInterceptor(
                DanbooruAuthInterceptor(profile.name, profile.apiKey)
            )
            .addNetworkInterceptor(ForceCacheResponseInterceptor())
            .build()

        val preferredOkHttpClient = when {
            profile.id != 0 -> okHttpClientWithAuth
            else -> okHttpClient
        }

        clientAuth = buildClient(preferredOkHttpClient, actualHost)
        clientNoAuth = buildClient(okHttpClient, actualHost)
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
            .apply {
                if (forceRefresh) {
                    noCache()
                }
            }
            .build()
    }

    private fun isInSafeMode(): Boolean {
        return environment.safeMode || activeUser.danbooru.safeMode || activeUser.mikansei.postsRatingFilter == RatingPreference.GENERAL_ONLY
    }

    override fun removeCachedEndpoints() {
        val cacheUrlIterator = cache.urls()

        while (cacheUrlIterator.hasNext()) {
            val nextUrl = cacheUrlIterator.next()

            val isUrlInList = cacheClearedEndpoints.any {
                nextUrl.contains(it)
            }

            if (isUrlInList) {
                cacheUrlIterator.remove()
            }
        }
    }

    override suspend fun getPost(id: Int): Result<Post> = resultOf {
        clientAuth.getPost(id)
    }.mapSuccess(DanbooruPost::toPost)

    override suspend fun getPosts(tags: String, page: Int): Result<PostsResult> = resultOf {
        client.getPosts(tags, page)
    }.mapSuccess {
        PostsResult(
            posts = it.toPostList(),
            canLoadMore = it.size == Constants.POSTS_PER_PAGE,
        )
    }

    override suspend fun getTagsAutoComplete(query: String): Result<List<Tag>> = resultOf {
        client.getTagsAutoComplete(query)
    }.mapSuccess {
        it.toTagList()
    }

    override suspend fun getTags(tags: List<String>): Result<List<Tag>> = resultOf {
        client.getTags(tags)
    }.mapSuccess {
        it.toTagList()
    }

    override suspend fun isPostInFavorites(postId: Int, userId: Int): Result<Boolean> = resultOf {
        client.getFavorites(postId, userId)
    }.mapSuccess { favorites ->
        favorites.any { it.postId == postId && it.userId == userId }
    }

    override suspend fun getProfile(): Result<Profile> = resultOf {
        client.getProfile()
    }.mapSuccess {
        it.toProfile()
    }

    override suspend fun login(name: String, apiKey: String): Result<Profile> = resultOf {
        clientNoAuth.getProfile(
            credentials = Credentials.basic(
                username = name,
                password = apiKey,
            )
        )
    }.mapSuccess {
        it.toProfile()
    }

    override suspend fun getUser(id: Int): Result<User> = resultOf {
        client.getUser(id)
    }.mapSuccess {
        it.toUser()
    }

    override suspend fun getUsers(ids: List<Int>): Result<Map<Int, User>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserSettings(id: Int, field: ProfileSettingsField): Result<Unit> = resultOf {
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

    override suspend fun getFavoriteGroups(creatorId: Int, forceRefresh: Boolean): Result<List<Favorite>> = resultOf {
        client.getFavoriteGroups(creatorId, getCacheControl(forceRefresh))
    }.mapSuccess { favoriteGroups ->
        favoriteGroups.sortedByDescending { it.updatedAt }.toFavoriteList()
    }

    override suspend fun getPostsByIds(ids: List<Int>, forceCache: Boolean, forceRefresh: Boolean): Result<List<Post>> = resultOf {
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

    override suspend fun addToFavorites(postId: Int): Result<Unit> = resultOf {
        client.addToFavorites(postId)
    }

    override suspend fun deleteFromFavorites(postId: Int): Result<Unit> = resultOf {
        client.deleteFromFavorites(postId)
    }

    override suspend fun getPostVote(postId: Int, userId: Int): Result<PostVote> = resultOf {
        client.getPostVotes(postId, userId)
    }.mapSuccess { postVotes ->
        postVotes.toPostVote(postId, userId)
    }

    override suspend fun votePost(postId: Int, score: PostVote.Status): Result<Unit> = resultOf {
        when (score) {
            PostVote.Status.NONE -> client.unvotePost(postId)
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

    override suspend fun addPostToFavoriteGroup(favoriteGroupId: Int, postId: Int): Result<Unit> = resultOf {
        client.addPostToFavoriteGroup(favoriteGroupId, postId)
    }

    override suspend fun removePostFromFavoriteGroup(favoriteGroupId: Int, postId: Int): Result<Unit> = resultOf {
        client.removePostFromFavoriteGroup(favoriteGroupId, postId)
    }

    override suspend fun createNewFavoriteGroup(name: String, postIds: List<Int>): Result<Favorite> = resultOf {
        client.createNewFavoriteGroup(name, postIds.joinToString(separator = " "))
    }.mapSuccess {
        it.toFavorite()
    }

    override suspend fun editFavoriteGroup(favoriteGroupId: Int, name: String, postIds: List<Int>): Result<Unit> = resultOf {
        client.editFavoriteGroup(favoriteGroupId, name, postIds.joinToString(separator = " "))
    }

    override suspend fun deleteFavoriteGroup(favoriteGroupId: Int): Result<Unit> = resultOf {
        client.deleteFavoriteGroup(favoriteGroupId)
    }

    override suspend fun getSavedSearches(forceRefresh: Boolean): Result<SavedSearch.Result> {
        val response = client.getSavedSearches(cacheControl = getCacheControl(forceRefresh))

        return resultOf {
            response
        }.mapSuccess {
            SavedSearch.Result(
                items = it.toSavedSearchList(),
                isFromCache = response.raw().cacheResponse != null
                        && response.raw().networkResponse == null,
            )
        }
    }

    override suspend fun createNewSavedSearch(
        query: String,
        labels: List<String>,
    ): Result<Unit> = resultOf {
        client.createNewSavedSearch(
            query = query,
            labels = labels.joinToString(separator = " "),
        )
    }.mapSuccess {
        it.toSavedSearch()
    }

    override suspend fun editSavedSearch(savedSearch: SavedSearch): Result<Unit> = resultOf {
        client.editSavedSearch(
            id = savedSearch.id,
            query = savedSearch.query,
            labels = savedSearch.labels.joinToString(separator = " "),
        )
    }

    override suspend fun deleteSavedSearch(id: Int): Result<Unit> = resultOf {
        client.deleteSavedSearch(id)
    }
}
