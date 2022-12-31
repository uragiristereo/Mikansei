package com.uragiristereo.mejiboard.data.source.danbooru

import com.google.gson.GsonBuilder
import com.uragiristereo.mejiboard.common.Constants
import com.uragiristereo.mejiboard.data.source.BooruSources
import com.uragiristereo.mejiboard.data.source.danbooru.model.toPostList
import com.uragiristereo.mejiboard.data.source.danbooru.model.toTagList
import com.uragiristereo.mejiboard.domain.entity.source.post.PostsResult
import com.uragiristereo.mejiboard.domain.entity.source.post.Rating
import com.uragiristereo.mejiboard.domain.entity.source.tag.TagsResult
import com.uragiristereo.mejiboard.domain.repository.BooruSourceRepository
import okhttp3.OkHttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DanbooruRepository(okHttpClient: OkHttpClient) : BooruSourceRepository, KoinComponent {
    override val source = BooruSources.Danbooru

    private val gson = GsonBuilder()
        .setDateFormat(source.dateFormat)
        .create()

    private val client = Retrofit.Builder()
        .baseUrl(source.baseUrl(get()))
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
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
