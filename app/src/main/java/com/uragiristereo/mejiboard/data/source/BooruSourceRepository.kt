package com.uragiristereo.mejiboard.data.source

import com.uragiristereo.mejiboard.domain.entity.source.BooruSource
import com.uragiristereo.mejiboard.domain.entity.source.post.PostsResult
import com.uragiristereo.mejiboard.domain.entity.source.post.Rating
import com.uragiristereo.mejiboard.domain.entity.source.tag.TagsResult

interface BooruSourceRepository {
    val source: BooruSource

    suspend fun getPosts(tags: String, page: Int, filters: List<Rating>): PostsResult

    suspend fun searchTerm(term: String, filters: List<Rating>): TagsResult

    suspend fun getTags(tags: List<String>): TagsResult
}
