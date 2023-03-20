package com.uragiristereo.mikansei.core.danbooru.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.uragiristereo.mejiboard.core.model.result.Result
import com.uragiristereo.mejiboard.core.model.result.mapSuccess
import com.uragiristereo.mejiboard.core.model.result.resultFlow
import com.uragiristereo.mejiboard.core.network.NetworkRepository
import com.uragiristereo.mejiboard.core.preferences.model.DanbooruSubdomain
import com.uragiristereo.mikansei.core.danbooru.DanbooruApi
import com.uragiristereo.mikansei.core.danbooru.model.post.DanbooruPost
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
open class DanbooruRepositoryImpl(
    private val networkRepository: NetworkRepository,
) : DanbooruRepository {
    open val subdomain = DanbooruSubdomain.DANBOORU

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val client by lazy {
        Retrofit.Builder()
            .baseUrl(subdomain.baseUrl)
            .client(networkRepository.okHttpClient)
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

    override suspend fun isPostInFavorites(postId: Int, userId: Int): Flow<Result<Boolean>> = resultFlow {
        client.getFavorites(postId, userId)
    }.mapSuccess { favorites ->
        favorites.any { it.postId == postId && it.userId == userId }
    }
}



