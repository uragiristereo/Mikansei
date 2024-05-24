package com.uragiristereo.mikansei.core.danbooru

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.uragiristereo.mikansei.core.danbooru.interceptor.DanbooruAuthInterceptor
import com.uragiristereo.mikansei.core.danbooru.interceptor.DanbooruHostInterceptor
import com.uragiristereo.mikansei.core.danbooru.interceptor.ForceCacheResponseInterceptor
import com.uragiristereo.mikansei.core.danbooru.interceptor.ForceRefreshInterceptor
import com.uragiristereo.mikansei.core.danbooru.interceptor.UserDelegationInterceptor
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
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostVote
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostsResult
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.ProfileSettingsField
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.SavedSearch
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.User
import com.uragiristereo.mikansei.core.domain.module.network.NetworkRepository
import com.uragiristereo.mikansei.core.model.CacheUtil
import com.uragiristereo.mikansei.core.model.Constants
import com.uragiristereo.mikansei.core.model.danbooru.DanbooruHost
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.preferences.user.DetailSizePreference
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import com.uragiristereo.mikansei.core.model.result.resultOf
import com.uragiristereo.mikansei.core.resources.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.util.zip.GZIPInputStream

@OptIn(ExperimentalSerializationApi::class)
class DanbooruRepositoryImpl(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    networkRepository: NetworkRepository,
    authInterceptor: DanbooruAuthInterceptor,
    userDelegationInterceptor: UserDelegationInterceptor,
    private val hostInterceptor: DanbooruHostInterceptor,
) : DanbooruRepository {
    override var unsafeTags: List<String> = listOf()

    override val host: DanbooruHost
        get() = hostInterceptor.host

    private val cacheClearedEndpoints = listOf(
        "/saved_searches.json",
        "/favorite_groups.json",
    )

    private val cache = CacheUtil.createDefaultCache(context = context, path = "http_cache")

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private val okHttpClient = networkRepository.okHttpClient.newBuilder()
        .cache(cache)
        .addInterceptor(hostInterceptor)
        .addNetworkInterceptor(authInterceptor)
        .addNetworkInterceptor(userDelegationInterceptor)
        .addNetworkInterceptor(ForceCacheResponseInterceptor)
        .addInterceptor(ForceRefreshInterceptor)
        .build()

    private val client = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(DanbooruHost.Safebooru.getBaseUrl())
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(DanbooruApi::class.java)

    init {
        loadUnsafeTags()
    }

    private fun loadUnsafeTags() {
        coroutineScope.launch {
            val rawResource = context.resources.openRawResource(R.raw.unsafe_tags)

            val stream = withContext(Dispatchers.IO) {
                GZIPInputStream(rawResource)
            }

            val text = stream.bufferedReader().use { it.readText() }

            unsafeTags = text.split('\n')
        }
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
        client.getPost(id)
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
        client.login(
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

    override suspend fun updateUserSettings(
        id: Int,
        field: ProfileSettingsField,
    ): Result<Unit> = resultOf {
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

    override suspend fun getFavoriteGroups(
        creatorId: Int,
        forceRefresh: Boolean,
    ): Result<List<Favorite>> = resultOf {
        client.getFavoriteGroups(creatorId, forceRefresh = forceRefresh)
    }.mapSuccess { favoriteGroups ->
        favoriteGroups.sortedByDescending { it.updatedAt }.toFavoriteList()
    }

    override suspend fun getPostsByIds(
        ids: List<Int>,
        forceCache: Boolean,
        forceRefresh: Boolean,
    ): Result<List<Post>> = resultOf {
        val separated = ids.joinToString(separator = ",")

        client.getPosts(
            tags = "id:$separated",
            pageId = 1,
            forceCache = forceCache,
            forceRefresh = forceRefresh,
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

    override suspend fun addPostToFavoriteGroup(
        favoriteGroupId: Int,
        postId: Int,
    ): Result<Unit> = resultOf {
        client.addPostToFavoriteGroup(favoriteGroupId, postId)
    }

    override suspend fun removePostFromFavoriteGroup(
        favoriteGroupId: Int,
        postId: Int,
    ): Result<Unit> = resultOf {
        client.removePostFromFavoriteGroup(favoriteGroupId, postId)
    }

    override suspend fun createNewFavoriteGroup(
        name: String,
        postIds: List<Int>,
    ): Result<Favorite> = resultOf {
        client.createNewFavoriteGroup(name, postIds.joinToString(separator = " "))
    }.mapSuccess {
        it.toFavorite()
    }

    override suspend fun editFavoriteGroup(
        favoriteGroupId: Int,
        name: String,
        postIds: List<Int>,
    ): Result<Unit> = resultOf {
        client.editFavoriteGroup(favoriteGroupId, name, postIds.joinToString(separator = " "))
    }

    override suspend fun deleteFavoriteGroup(favoriteGroupId: Int): Result<Unit> = resultOf {
        client.deleteFavoriteGroup(favoriteGroupId)
    }

    override suspend fun getSavedSearches(forceRefresh: Boolean): Result<SavedSearch.Result> {
        val response = client.getSavedSearches(forceRefresh = forceRefresh)

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
