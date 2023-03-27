package com.uragiristereo.mikansei.core.danbooru.repository

import com.uragiristereo.mikansei.core.danbooru.model.post.DanbooruPost
import com.uragiristereo.mikansei.core.danbooru.model.tag.DanbooruTag
import com.uragiristereo.mikansei.core.danbooru.model.tag.DanbooruTagAutoComplete
import com.uragiristereo.mikansei.core.danbooru.model.user.DanbooruUser
import com.uragiristereo.mikansei.core.model.result.Result
import kotlinx.coroutines.flow.Flow

interface DanbooruRepository {
    suspend fun getPost(id: Int): Flow<Result<DanbooruPost>>

    suspend fun getPosts(tags: String, page: Int): Flow<Result<List<DanbooruPost>>>

    suspend fun getTagsAutoComplete(query: String): Flow<Result<List<DanbooruTagAutoComplete>>>

    suspend fun getTags(tags: List<String>): Flow<Result<List<DanbooruTag>>>

    suspend fun isPostInFavorites(postId: Int, userId: Int): Flow<Result<Boolean>>

    suspend fun login(name: String, apiKey: String): Flow<Result<Boolean>>

    suspend fun getUser(userId: Int): Flow<Result<DanbooruUser>>

    suspend fun getUsers(userIds: List<Int>): Flow<Result<Map<Int, DanbooruUser>>>
}
