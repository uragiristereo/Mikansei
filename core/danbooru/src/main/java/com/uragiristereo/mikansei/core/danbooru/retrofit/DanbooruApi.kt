package com.uragiristereo.mikansei.core.danbooru.retrofit

import com.uragiristereo.mikansei.core.danbooru.model.favorite.DanbooruFavorite
import com.uragiristereo.mikansei.core.danbooru.model.favorite.DanbooruFavoriteGroup
import com.uragiristereo.mikansei.core.danbooru.model.post.DanbooruPost
import com.uragiristereo.mikansei.core.danbooru.model.post.vote.DanbooruPostVote
import com.uragiristereo.mikansei.core.danbooru.model.tag.DanbooruTag
import com.uragiristereo.mikansei.core.danbooru.model.tag.DanbooruTagAutoComplete
import com.uragiristereo.mikansei.core.danbooru.model.user.DanbooruProfile
import com.uragiristereo.mikansei.core.danbooru.model.user.DanbooruUser
import com.uragiristereo.mikansei.core.danbooru.model.user.field.DanbooruUserField
import com.uragiristereo.mikansei.core.model.Constants
import okhttp3.CacheControl
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
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
        @Header("force-cache") forceCache: Boolean = false,
        @Header("cache-control") cacheControl: String = "",
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

    @PATCH("/users/{id}.json")
    suspend fun updateUserSettings(
        @Path("id") id: Int,
        @Body data: DanbooruUserField,
    ): Response<Unit>

    @GET("/favorite_groups.json")
    suspend fun getFavoriteGroups(
        @Query("search[creator_id]") creatorId: Int,
        @Header("cache-control") cacheControl: CacheControl,
        @Header("force-cache") forceCache: Boolean = true,
    ): Response<List<DanbooruFavoriteGroup>>

    @POST("/favorites.json")
    suspend fun addToFavorites(
        @Query("post_id") postId: Int,
    ): Response<Unit>

    @DELETE("/favorites/{post_id}.json")
    suspend fun deleteFromFavorites(
        @Path("post_id") postId: Int,
    ): Response<Unit>

    @GET("/post_votes.json")
    suspend fun getPostVotes(
        @Query("search[post_id]") postId: Int,
        @Query("search[user_id]") userId: Int,
    ): Response<List<DanbooruPostVote>>

    @POST("/posts/{post_id}/votes.json")
    suspend fun votePost(
        @Path("post_id") postId: Int,
        @Query("score") score: Int,
    ): Response<Unit>

    @DELETE("posts/{post_id}/votes.json")
    suspend fun unvotePost(
        @Path("post_id") postId: Int,
    ): Response<Unit>

    @PUT("/favorite_groups/{favorite_group_id}/add_post.json")
    suspend fun addPostToFavoriteGroup(
        @Path("favorite_group_id") favoriteGroupId: Int,
        @Query("post_id") postId: Int,
    ): Response<Unit>

    @PUT("/favorite_groups/{favorite_group_id}/remove_post.json")
    suspend fun removePostFromFavoriteGroup(
        @Path("favorite_group_id") favoriteGroupId: Int,
        @Query("post_id") postId: Int,
    ): Response<Unit>

    @POST("/favorite_groups.json")
    suspend fun createNewFavoriteGroup(
        @Query("favorite_group[name]") name: String,
        @Query("favorite_group[post_ids_string]") postIds: String,
    ): Response<DanbooruFavoriteGroup>
}
