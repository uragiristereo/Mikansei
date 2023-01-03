package com.uragiristereo.mejiboard.data.source.safebooruorg

import android.content.Context
import com.google.gson.GsonBuilder
import com.uragiristereo.mejiboard.common.Constants
import com.uragiristereo.mejiboard.data.source.BooruSourceRepository
import com.uragiristereo.mejiboard.data.source.BooruSources
import com.uragiristereo.mejiboard.data.source.gelbooru.GelbooruApi
import com.uragiristereo.mejiboard.data.source.gelbooru.model.toTagList
import com.uragiristereo.mejiboard.data.source.safebooruorg.model.toPostList
import com.uragiristereo.mejiboard.data.source.safebooruorg.model.toTagList
import com.uragiristereo.mejiboard.domain.entity.source.post.PostsResult
import com.uragiristereo.mejiboard.domain.entity.source.post.Rating
import com.uragiristereo.mejiboard.domain.entity.source.tag.TagsResult
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SafebooruOrgRepository(
    private val context: Context,
    okHttpClient: OkHttpClient,
) : BooruSourceRepository {
    override val source = BooruSources.SafebooruOrg

    private val gson = GsonBuilder()
        .setDateFormat(source.dateFormat)
        .create()

    private val retrofitBuilder = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))

    private val client = retrofitBuilder
        .baseUrl(BooruSources.SafebooruOrg.baseUrl(context))
        .build()
        .create(SafebooruOrgApi::class.java)

    private val clientGelbooru = retrofitBuilder
        .baseUrl(source.baseUrl(context))
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
