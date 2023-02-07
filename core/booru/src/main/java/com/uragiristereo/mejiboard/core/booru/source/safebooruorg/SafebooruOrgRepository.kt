package com.uragiristereo.mejiboard.core.booru.source.safebooruorg

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.uragiristereo.mejiboard.core.booru.source.BooruSourceRepository
import com.uragiristereo.mejiboard.core.booru.source.gelbooru.GelbooruApi
import com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.toTagList
import com.uragiristereo.mejiboard.core.booru.source.safebooruorg.model.toPostList
import com.uragiristereo.mejiboard.core.booru.source.safebooruorg.model.toTagList
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
class SafebooruOrgRepository(
    private val context: Context,
    okHttpClient: OkHttpClient,
) : BooruSourceRepository {
    override val source = BooruSource.SafebooruOrg

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofitBuilder = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType())
        )

    private val client = retrofitBuilder
        .baseUrl(source.baseUrl(context))
        .build()
        .create(SafebooruOrgApi::class.java)

    private val clientGelbooru = retrofitBuilder
        .baseUrl(BooruSource.Gelbooru.baseUrl(context))
        .build()
        .create(GelbooruApi::class.java)

    override suspend fun getPosts(tags: String, page: Int, filters: List<Rating>): PostsResult {
        val response = client.getPosts(
            tags = tags,
            pageId = page,
            postsPerPage = Constants.POSTS_PER_PAGE,
        )

        if (response.isSuccessful) {
            val posts = response.body()!!.toPostList(context)
            return PostsResult(
                data = posts,
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
        val tagsStr = tags.joinToString(separator = " ")

        // we still need to use a part of Gelbooru's API since Safebooru.org doesn't provide it
        // it's fairly inaccurate but did the job
        val response = clientGelbooru.getTags(tagsStr)

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
