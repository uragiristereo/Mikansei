package com.uragiristereo.mejiboard.core.booru.source.danbooru

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.uragiristereo.mejiboard.core.booru.source.BooruSourceRepository
import com.uragiristereo.mejiboard.core.booru.source.danbooru.model.toPostList
import com.uragiristereo.mejiboard.core.booru.source.danbooru.model.toTagList
import com.uragiristereo.mejiboard.core.model.Constants
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
class DanbooruRepository(
    context: Context,
    okHttpClient: OkHttpClient,
) : BooruSourceRepository {
    override val source = BooruSource.Danbooru

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val client = Retrofit.Builder()
        .baseUrl(source.baseUrl(context))
        .client(okHttpClient)
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType())
        )
        .build()
        .create(DanbooruApi::class.java)

    override suspend fun getPosts(tags: String, page: Int, filters: List<Rating>): PostsResult {
        val response = client.getPosts(
            tags = tags,
            pageId = page + 1,
            postsPerPage = Constants.POSTS_PER_PAGE,
        )

        if (response.isSuccessful) {
            val posts = response.body()!!.toPostList()

            val filtered = posts.filter { post ->
                !(post.rating in filters && post.id != 0)
            }

            return PostsResult(
                data = filtered,
                canLoadMore = posts.size == Constants.POSTS_PER_PAGE,
            )
        }

        return PostsResult(
            errorMessage = "${response.code()}: ${response.errorBody()!!.string()}",
        )
    }

    override suspend fun searchTerm(term: String, filters: List<Rating>): TagsResult {
        val response = client.searchTerm(term)

        if (response.isSuccessful) {
            return TagsResult(
                data = response.body()!!.toTagList(),
            )
        }

        return TagsResult(
            errorMessage = "${response.code()}: ${response.errorBody()!!.string()}",
        )
    }

    override suspend fun getTags(tags: List<String>): TagsResult {
        val response = client.getTags(tags)

        if (response.isSuccessful) {
            return TagsResult(
                data = response.body()!!.toTagList(),
            )
        }

        return TagsResult(
            errorMessage = "${response.code()}: ${response.errorBody()!!.string()}",
        )
    }
}
