package com.uragiristereo.mikansei.core.domain.module.danbooru

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.AutoComplete
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostVote
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostsResult
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.ProfileSettingsField
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.SavedSearch
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.User
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Wiki
import com.uragiristereo.mikansei.core.model.danbooru.DanbooruHost
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.result.Result

interface DanbooruRepository {
    val host: DanbooruHost
    val unsafeTags: List<String>

    fun removeCachedEndpoints()

    suspend fun getPost(id: Int): Result<Post>

    suspend fun getPosts(tags: String, page: Int, limit: Int): Result<PostsResult>

    suspend fun getTagsAutoComplete(query: String): Result<List<Tag>>

    suspend fun getTags(tags: List<String>): Result<List<Tag>>

    suspend fun isPostInFavorites(postId: Int, userId: Int): Result<Boolean>

    suspend fun getProfile(): Result<Profile>

    suspend fun login(name: String, apiKey: String): Result<Profile>

    suspend fun getUser(id: Int): Result<User>

    suspend fun getUsers(ids: List<Int>): Result<Map<Int, User>>

    suspend fun updateUserSettings(id: Int, field: ProfileSettingsField): Result<Unit>

    suspend fun getFavoriteGroups(creatorId: Int, forceRefresh: Boolean): Result<List<Favorite>>

    suspend fun getPostsByIds(ids: List<Int>, forceCache: Boolean, forceRefresh: Boolean): Result<List<Post>>

    suspend fun addToFavorites(postId: Int): Result<Unit>

    suspend fun deleteFromFavorites(postId: Int): Result<Unit>

    suspend fun getPostVote(postId: Int, userId: Int): Result<PostVote>

    suspend fun votePost(postId: Int, score: PostVote.Status): Result<Unit>

    suspend fun addPostToFavoriteGroup(favoriteGroupId: Int, postId: Int): Result<Unit>

    suspend fun removePostFromFavoriteGroup(favoriteGroupId: Int, postId: Int): Result<Unit>

    suspend fun createNewFavoriteGroup(name: String, postIds: List<Int>): Result<Favorite>

    suspend fun editFavoriteGroup(favoriteGroupId: Int, name: String, postIds: List<Int>): Result<Unit>

    suspend fun deleteFavoriteGroup(favoriteGroupId: Int): Result<Unit>

    suspend fun getSavedSearches(forceRefresh: Boolean): Result<SavedSearch.Result>

    suspend fun createNewSavedSearch(query: String, labels: List<String>): Result<Unit>

    suspend fun editSavedSearch(savedSearch: SavedSearch): Result<Unit>

    suspend fun deleteSavedSearch(id: Int): Result<Unit>

    suspend fun deactivateAccount(userId: Int, password: String): Result<Unit>

    suspend fun getAutoComplete(query: String, searchType: AutoComplete.SearchType): Result<List<AutoComplete>>

    suspend fun parseDtextAsHtml(dtext: String): Result<String>

    suspend fun getWiki(tag: String): Result<Wiki?>
}
