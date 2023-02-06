package com.uragiristereo.mejiboard.core.booru.source.yandere

import com.uragiristereo.mejiboard.core.booru.source.yandere.model.post.YanderePost
import com.uragiristereo.mejiboard.core.booru.source.yandere.model.search.YandereSearch
import com.uragiristereo.mejiboard.core.booru.source.yandere.model.tag.YandereTags
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YandereApi {
    @GET("/post.json")
    suspend fun getPosts(
        @Query("tags") tags: String,
        @Query("page") pageId: Int,
        @Query("limit") postsPerPage: Int,
    ): Response<List<YanderePost>>

    @GET("/tag.json?order=count&limit=10")
    suspend fun searchTerm(
        @Query("name") term: String,
    ): Response<List<YandereSearch>>


    @GET("/tag/related.json")
    suspend fun getRelatedTags(
        @Query("tags") tags: String,
    ): Response<YandereTags>
}
