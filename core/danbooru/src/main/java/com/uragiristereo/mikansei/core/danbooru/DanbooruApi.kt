package com.uragiristereo.mikansei.core.danbooru

import com.uragiristereo.mikansei.core.danbooru.model.favorite.DanbooruFavorite
import com.uragiristereo.mikansei.core.danbooru.model.post.DanbooruPost
import com.uragiristereo.mikansei.core.danbooru.model.tag.DanbooruTag
import com.uragiristereo.mikansei.core.danbooru.model.tag.DanbooruTagAutoComplete
import com.uragiristereo.mikansei.core.danbooru.model.user.DanbooruProfile
import com.uragiristereo.mikansei.core.danbooru.model.user.DanbooruUser
import com.uragiristereo.mikansei.core.model.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
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
        @Query("limit") limit: Int = 20,
    ): Response<List<DanbooruTagAutoComplete>>

    @GET("/tags.json")
    suspend fun getTags(
        @Query("search[name][]") tags: List<String>,
        @Query("limit") limit: Int = 100,
    ): Response<List<DanbooruTag>>

    @GET("/favorites.json")
    suspend fun getFavorites(
        @Query("search[post_id]") postId: Int,
        @Query("search[user_id]") userId: Int,
    ): Response<List<DanbooruFavorite>>

    @GET("/profile.json")
    suspend fun getProfile(
        @Header("Authorization") credentials: String? = null,
    ): Response<DanbooruProfile>

    @GET("/users/{id}.json")
    suspend fun getUser(@Path("id") id: Int): Response<DanbooruUser>
}
