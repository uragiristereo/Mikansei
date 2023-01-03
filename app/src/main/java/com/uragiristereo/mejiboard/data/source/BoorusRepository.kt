package com.uragiristereo.mejiboard.data.source

import com.uragiristereo.mejiboard.domain.entity.source.BooruSource
import com.uragiristereo.mejiboard.domain.entity.source.post.PostsResult
import com.uragiristereo.mejiboard.domain.entity.source.tag.TagsResult

interface BoorusRepository {
    var boorus: Map<BooruSource, BooruSourceRepository>

    val currentBooru: BooruSource

    suspend fun getPosts(tags: String, page: Int): PostsResult

    suspend fun searchTerm(term: String): TagsResult

    suspend fun getTags(tags: List<String>): TagsResult
}
