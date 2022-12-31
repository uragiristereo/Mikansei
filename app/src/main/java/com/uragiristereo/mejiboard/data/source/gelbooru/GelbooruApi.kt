package com.uragiristereo.mejiboard.data.source.gelbooru

import com.uragiristereo.mejiboard.data.source.gelbooru.model.post.GelbooruPostsResult
import com.uragiristereo.mejiboard.data.source.gelbooru.model.search.GelbooruSearch
import com.uragiristereo.mejiboard.data.source.gelbooru.model.tag.GelbooruTagsResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GelbooruApi {
    @GET("/index.php?page=dapi&s=post&q=index&json=1")
    suspend fun getPosts(
        @Query("tags") tags: String,
        @Query("pid") pageId: Int,
        @Query("limit") postsPerPage: Int,
    ): Response<GelbooruPostsResult>

    @GET("/index.php?page=autocomplete2&type=tag_query&limit=10")
    suspend fun searchTerm(@Query("term") term: String): Response<List<GelbooruSearch>>

    @GET("/index.php?page=dapi&s=tag&q=index&json=1")
    suspend fun getTags(@Query("names") tags: String): Response<GelbooruTagsResult>
}