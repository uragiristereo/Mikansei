package com.uragiristereo.mejiboard.core.booru.source

import com.uragiristereo.mejiboard.core.model.booru.BooruSource
import com.uragiristereo.mejiboard.core.model.booru.post.PostsResult
import com.uragiristereo.mejiboard.core.model.booru.post.Rating
import com.uragiristereo.mejiboard.core.model.booru.tag.TagsResult

interface BooruSourceRepository {
    val source: BooruSource

    suspend fun getPosts(tags: String, page: Int, filters: List<Rating>): PostsResult

    suspend fun searchTerm(term: String, filters: List<Rating>): TagsResult

    suspend fun getTags(tags: List<String>): TagsResult
}
