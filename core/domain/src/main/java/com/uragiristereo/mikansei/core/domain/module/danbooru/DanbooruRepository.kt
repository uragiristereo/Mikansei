package com.uragiristereo.mikansei.core.domain.module.danbooru

import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostVote
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.PostsResult
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Profile
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.ProfileSettingsField
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.User
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.result.Result
import kotlinx.coroutines.flow.Flow

interface DanbooruRepository {
    val isProd: Boolean
    val unsafeTags: List<String>

    fun getPost(id: Int): Flow<Result<Post>>

    fun getPosts(tags: String, page: Int): Flow<Result<PostsResult>>

    fun getTagsAutoComplete(query: String): Flow<Result<List<Tag>>>

    fun getTags(tags: List<String>): Flow<Result<List<Tag>>>

    fun isPostInFavorites(postId: Int, userId: Int): Flow<Result<Boolean>>

    fun getProfile(): Flow<Result<Profile>>

    fun login(name: String, apiKey: String): Flow<Result<Profile>>

    fun getUser(id: Int): Flow<Result<User>>

    fun getUsers(ids: List<Int>): Flow<Result<Map<Int, User>>>

    fun updateUserSettings(id: Int, field: ProfileSettingsField): Flow<Result<Unit>>

    fun getFavoriteGroups(creatorId: Int, forceRefresh: Boolean): Flow<Result<List<Favorite>>>

    fun getPostsByIds(ids: List<Int>, forceCache: Boolean, forceRefresh: Boolean): Flow<Result<List<Post>>>

    fun addToFavorites(postId: Int): Flow<Result<Unit>>

    fun deleteFromFavorites(postId: Int): Flow<Result<Unit>>

    fun getPostVote(postId: Int, userId: Int): Flow<Result<PostVote>>

    fun votePost(postId: Int, score: PostVote.Status): Flow<Result<Unit>>

    fun addPostToFavoriteGroup(favoriteGroupId: Int, postId: Int): Flow<Result<Unit>>

    fun removePostFromFavoriteGroup(favoriteGroupId: Int, postId: Int): Flow<Result<Unit>>

    fun createNewFavoriteGroup(name: String, postIds: List<Int>): Flow<Result<Favorite>>
}
