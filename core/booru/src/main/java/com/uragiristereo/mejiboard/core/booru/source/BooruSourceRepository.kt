package com.uragiristereo.mejiboard.core.booru.source

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.uragiristereo.mejiboard.core.model.booru.BooruSource
import com.uragiristereo.mejiboard.core.model.booru.post.PostsResult
import com.uragiristereo.mejiboard.core.model.booru.post.Rating
import com.uragiristereo.mejiboard.core.model.booru.tag.TagsResult
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
interface BooruSourceRepository {
    val source: BooruSource

    val json: Json
        get() = Json {
            ignoreUnknownKeys = true
        }

    fun <T> buildClient(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        service: Class<T>,
    ): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(service)
    }

    suspend fun getPosts(tags: String, page: Int, filters: List<Rating>): PostsResult

    suspend fun searchTerm(term: String, filters: List<Rating>): TagsResult

    suspend fun getTags(tags: List<String>): TagsResult
}
