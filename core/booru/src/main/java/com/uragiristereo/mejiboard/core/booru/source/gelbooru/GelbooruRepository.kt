package com.uragiristereo.mejiboard.core.booru.source.gelbooru

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.uragiristereo.mejiboard.core.booru.source.BooruSourceRepository
import com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.search.GelbooruSearch
import com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.toPostList
import com.uragiristereo.mejiboard.core.booru.source.gelbooru.model.toTagList
import com.uragiristereo.mejiboard.core.booru.source.safebooruorg.SafebooruOrgApi
import com.uragiristereo.mejiboard.core.booru.source.safebooruorg.model.search.SafebooruOrgSearch
import com.uragiristereo.mejiboard.core.booru.source.safebooruorg.model.toTagList
import com.uragiristereo.mejiboard.core.model.Constants
import com.uragiristereo.mejiboard.core.model.RatingFilter
import com.uragiristereo.mejiboard.core.model.booru.BooruSources
import com.uragiristereo.mejiboard.core.model.booru.post.PostsResult
import com.uragiristereo.mejiboard.core.model.booru.post.Rating
import com.uragiristereo.mejiboard.core.model.booru.tag.TagsResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
class GelbooruRepository(
    private val context: Context,
    okHttpClient: OkHttpClient,
) : BooruSourceRepository {
    override val source = BooruSources.Gelbooru

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
        .create(GelbooruApi::class.java)

    private val clientSafe = retrofitBuilder
        .baseUrl(BooruSources.SafebooruOrg.baseUrl(context))
        .build()
        .create(SafebooruOrgApi::class.java)

    override suspend fun getPosts(tags: String, page: Int, filters: List<Rating>): PostsResult {
        // Gelbooru allows unlimited tags to search so we can filter ratings from server
        val filterTags = when (filters) {
            RatingFilter.GENERAL_ONLY -> " rating:general"
            RatingFilter.SAFE -> " -rating:questionable -rating:explicit"
            RatingFilter.NO_EXPLICIT -> " -rating:explicit"
            RatingFilter.UNFILTERED -> ""
            else -> " rating:general"
        }

        val response = client.getPosts(
            tags = tags + filterTags,
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
        if (filters == RatingFilter.NO_EXPLICIT || filters == RatingFilter.UNFILTERED) {
            val response = client.searchTerm(term)

            if (response.isSuccessful) {
                return TagsResult(
                    data = response.body()!!.toTagList(),
                )
            }

            return TagsResult(
                errorMessage = "${response.code()}: ${response.errorBody()!!.string()}",
            )
        } else {
            return searchTermSafe(term)
        }
    }

    override suspend fun getTags(tags: List<String>): TagsResult {
        val tagsStr = tags.joinToString(separator = " ")

        val response = client.getTags(tagsStr)

        if (response.isSuccessful) {
            return TagsResult(
                data = response.body()!!.toTagList(),
            )
        }

        return TagsResult(
            errorMessage = "${response.code()}: ${response.errorBody()!!.string()}",
        )
    }

    private suspend fun searchTermSafe(term: String): TagsResult {
        lateinit var response: Response<List<GelbooruSearch>>
        lateinit var responseFromSafebooruOrg: Response<List<SafebooruOrgSearch>>

        withContext(Dispatchers.IO) {
            val tasks = listOf(
                async {
                    // we ONLY need to get tag's post count from Gelbooru
                    response = client.searchTerm(term)
                },
                async {
                    // we need to get tag list from SafebooruOrg
                    responseFromSafebooruOrg = clientSafe.searchTerm(term)
                },
            )

            tasks.awaitAll()
        }

        if (response.isSuccessful && responseFromSafebooruOrg.isSuccessful) {
            val gelbooruTagList = response.body()!!.toTagList()
            val mergedTagList = responseFromSafebooruOrg.body()!!
                .toTagList()
                .map { safebooruOrgTag ->
                    // merge both data
                    val gelbooruTag =
                        gelbooruTagList.firstOrNull { it.name == safebooruOrgTag.name }

                    gelbooruTag ?: safebooruOrgTag
                }

            return TagsResult(
                data = mergedTagList,
            )
        }

        return TagsResult(
            errorMessage = "${response.code()}: ${response.errorBody()!!.string()}",
        )
    }
}
