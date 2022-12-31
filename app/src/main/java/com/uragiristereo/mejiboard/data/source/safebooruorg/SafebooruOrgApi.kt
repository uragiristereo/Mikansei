package com.uragiristereo.mejiboard.data.source.safebooruorg

import com.uragiristereo.mejiboard.data.source.safebooruorg.model.posts.SafebooruOrgPost
import com.uragiristereo.mejiboard.data.source.safebooruorg.model.search.SafebooruOrgSearch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SafebooruOrgApi {
    @GET("/index.php?page=dapi&s=post&q=index&json=1")
    suspend fun getPosts(
        @Query("tags") tags: String,
        @Query("pid") pageId: Int,
        @Query("limit") postsPerPage: Int,
    ): Response<List<SafebooruOrgPost>>

    @GET("/autocomplete.php")
    suspend fun searchTerm(@Query("q") term: String): Response<List<SafebooruOrgSearch>>
}
