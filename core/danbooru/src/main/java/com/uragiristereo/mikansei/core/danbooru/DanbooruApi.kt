package com.uragiristereo.mikansei.core.danbooru

import com.uragiristereo.mikansei.core.danbooru.annotation.NoAuth
import com.uragiristereo.mikansei.core.danbooru.annotation.NoSafeHost
import com.uragiristereo.mikansei.core.danbooru.model.favorite.DanbooruFavorite
import com.uragiristereo.mikansei.core.danbooru.model.favorite.DanbooruFavoriteGroup
import com.uragiristereo.mikansei.core.danbooru.model.post.DanbooruPost
import com.uragiristereo.mikansei.core.danbooru.model.post.vote.DanbooruPostVote
import com.uragiristereo.mikansei.core.danbooru.model.profile.DanbooruProfile
import com.uragiristereo.mikansei.core.danbooru.model.saved_search.DanbooruSavedSearch
import com.uragiristereo.mikansei.core.danbooru.model.tag.DanbooruTag
import com.uragiristereo.mikansei.core.danbooru.model.tag.DanbooruTagAutoComplete
import com.uragiristereo.mikansei.core.danbooru.model.tag.DanbooruTagPayload
import com.uragiristereo.mikansei.core.danbooru.model.user.DanbooruUser
import com.uragiristereo.mikansei.core.danbooru.model.user.field.DanbooruUserField
import com.uragiristereo.mikansei.core.model.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface DanbooruApi {
    @NoSafeHost
    @GET("/posts/{id}.json")
    suspend fun getPost(@Path("id") id: Int): Response<DanbooruPost>

    @GET("/posts.json")
    suspend fun getPosts(
        @Query("tags") tags: String,
        @Query("page") pageId: Int,
        @Query("limit") postsPerPage: Int = Constants.POSTS_PER_PAGE,
        @Header("force-cache") forceCache: Boolean = false,
        @Header("force-refresh") forceRefresh: Boolean = false,
    ): Response<List<DanbooruPost>>

    @NoAuth
    @GET("/autocomplete.json")
    suspend fun getTagsAutoComplete(
        @Query("search[query]") query: String,
        @Query("search[type]") type: String = "tag_query",
        @Query("limit") limit: Int = 20,
    ): Response<List<DanbooruTagAutoComplete>>

    @NoAuth
    @Headers("X-HTTP-Method-Override: get")
    @POST("/tags.json")
    suspend fun getTags(
        @Query("_method") method: String = "get",
        @Query("limit") limit: Int = 100,
        @Body body: DanbooruTagPayload,
    ): Response<List<DanbooruTag>>

    @GET("/favorites.json")
    suspend fun getFavorites(
        @Query("search[post_id]") postId: Int,
        @Query("search[user_id]") userId: Int,
    ): Response<List<DanbooruFavorite>>

    @GET("/profile.json")
    suspend fun getProfile(): Response<DanbooruProfile>

    @NoAuth
    @GET("/profile.json")
    suspend fun login(
        @Header("Authorization") credentials: String,
    ): Response<DanbooruProfile>

    @NoAuth
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
        @Header("force-refresh") forceRefresh: Boolean,
        @Header("force-cache") forceCache: Boolean = true,
        @Header("force-load-from-cache") forceLoadFromCache: Boolean = false,
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

    @PUT("/favorite_groups/{favorite_group_id}.json")
    suspend fun editFavoriteGroup(
        @Path("favorite_group_id") favoriteGroupId: Int,
        @Query("favorite_group[name]") name: String,
        @Query("favorite_group[post_ids_string]") postIds: String,
    ): Response<Unit>

    @DELETE("/favorite_groups/{favorite_group_id}.json")
    suspend fun deleteFavoriteGroup(
        @Path("favorite_group_id") favoriteGroupId: Int,
    ): Response<Unit>

    @GET("/saved_searches.json")
    suspend fun getSavedSearches(
        @Header("force-cache") forceCache: Boolean = true,
        @Header("force-refresh") forceRefresh: Boolean,
    ): Response<List<DanbooruSavedSearch>>

    @POST("/saved_searches.json")
    suspend fun createNewSavedSearch(
        @Query("saved_search[query]") query: String,
        @Query("saved_search[label_string]") labels: String,
    ): Response<DanbooruSavedSearch>

    @PUT("/saved_searches/{id}.json")
    suspend fun editSavedSearch(
        @Path("id") id: Int,
        @Query("saved_search[query]") query: String,
        @Query("saved_search[label_string]") labels: String,
    ): Response<Unit>

    @DELETE("/saved_searches/{id}.json")
    suspend fun deleteSavedSearch(@Path("id") id: Int): Response<Unit>

    @DELETE("/users/{id}.json")
    suspend fun deactivateAccount(
        @Path("id") userId: Int,
        @Query("user[password]") password: String,
    ): Response<Unit>
}
