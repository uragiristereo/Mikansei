package com.uragiristereo.mejiboard.core.booru.source.danbooru

import com.uragiristereo.mejiboard.core.booru.source.danbooru.model.post.DanbooruPost
import com.uragiristereo.mejiboard.core.booru.source.danbooru.model.search.DanbooruSearch
import com.uragiristereo.mejiboard.core.booru.source.danbooru.model.tag.DanbooruTag
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DanbooruApi {
    @GET("/posts.json")
    suspend fun getPosts(
        @Query("tags") tags: String,
        @Query("page") pageId: Int,
        @Query("limit") postsPerPage: Int,
    ): Response<List<DanbooruPost>>

    @GET("/autocomplete.json?&search[type]=tag_query&limit=10")
    suspend fun searchTerm(@Query("search[query]") term: String): Response<List<DanbooruSearch>>

    @GET("/tags.json")
    suspend fun getTags(@Query("search[name][]") tags: List<String>): Response<List<DanbooruTag>>
}
