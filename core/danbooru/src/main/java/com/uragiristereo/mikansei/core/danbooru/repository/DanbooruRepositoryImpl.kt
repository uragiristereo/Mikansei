package com.uragiristereo.mikansei.core.danbooru.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.uragiristereo.mejiboard.core.model.result.Result
import com.uragiristereo.mejiboard.core.model.result.mapSuccess
import com.uragiristereo.mejiboard.core.model.result.resultFlow
import com.uragiristereo.mejiboard.core.network.NetworkRepository
import com.uragiristereo.mejiboard.core.preferences.PreferencesRepository
import com.uragiristereo.mejiboard.core.preferences.model.DanbooruSubdomain
import com.uragiristereo.mejiboard.core.preferences.model.Preferences
import com.uragiristereo.mikansei.core.danbooru.DanbooruApi
import com.uragiristereo.mikansei.core.danbooru.model.post.DanbooruPost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
class DanbooruRepositoryImpl(
    private val networkRepository: NetworkRepository,
    preferencesRepository: PreferencesRepository,
) : DanbooruRepository {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    private var preferences = Preferences()

    private fun buildClient(subdomain: DanbooruSubdomain, dohEnabled: Boolean): DanbooruApi {
        return Retrofit.Builder()
            .baseUrl(subdomain.baseUrl)
            .client(networkRepository.getPreferredOkHttpClient(dohEnabled))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(DanbooruApi::class.java)
    }

    private var client: DanbooruApi = buildClient(
        subdomain = preferences.subdomain,
        dohEnabled = preferences.dohEnabled,
    )

    init {
        CoroutineScope(context = Dispatchers.Default + SupervisorJob()).launch {
            preferencesRepository.flowData.collect { newPreferences ->
                if (preferences.subdomain != newPreferences.subdomain) {
                    client = buildClient(
                        subdomain = newPreferences.subdomain,
                        dohEnabled = newPreferences.dohEnabled,
                    )
                }

                preferences = newPreferences
            }
        }
    }

    override suspend fun getPost(id: Int): Flow<Result<DanbooruPost>> = resultFlow {
        client.getPost(id)
    }

    override suspend fun getPosts(tags: String, pageId: Int) = resultFlow {
        client.getPosts(tags, pageId)
    }

    override suspend fun getTagsAutoComplete(query: String) = resultFlow {
        client.getTagsAutoComplete(query)
    }

    override suspend fun getTags(tags: List<String>) = resultFlow {
        client.getTags(tags)
    }

    override suspend fun isPostInFavorites(postId: Int, userId: Int): Flow<Result<Boolean>> = resultFlow {
        client.getFavorites(postId, userId)
    }.mapSuccess { favorites ->
        favorites.any { it.postId == postId && it.userId == userId }
    }
}


