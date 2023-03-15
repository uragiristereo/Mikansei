package com.uragiristereo.mikansei.core.danbooru

import com.uragiristereo.mejiboard.core.model.Constants
import com.uragiristereo.mikansei.core.danbooru.model.post.DanbooruPost
import com.uragiristereo.mikansei.core.danbooru.model.tag.DanbooruTag
import com.uragiristereo.mikansei.core.danbooru.model.tag.DanbooruTagAutoComplete
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DanbooruApi {
    @GET("/posts/{id}.json")
    suspend fun getPost(@Path("id") id: Int): Response<DanbooruPost>

    @GET("/posts.json")
    suspend fun getPosts(
        @Query("tags") tags: String,
        @Query("page") pageId: Int,
        @Query("limit") postsPerPage: Int = Constants.POSTS_PER_PAGE,
    ): Response<List<DanbooruPost>>

    @GET("/autocomplete.json")
    suspend fun getTagsAutoComplete(
        @Query("search[query]") query: String,
        @Query("search[type]") type: String = "tag_query",
        @Query("limit") limit: Int = 10,
    ): Response<List<DanbooruTagAutoComplete>>

    @GET("/tags.json")
    suspend fun getTags(
        @Query("search[name][]") tags: List<String>,
    ): Response<List<DanbooruTag>>
}
