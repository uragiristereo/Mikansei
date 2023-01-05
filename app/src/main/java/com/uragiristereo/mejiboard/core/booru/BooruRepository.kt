package com.uragiristereo.mejiboard.core.booru

import com.uragiristereo.mejiboard.core.booru.source.BooruSource
import com.uragiristereo.mejiboard.core.booru.source.BooruSourceRepository
import com.uragiristereo.mejiboard.domain.entity.booru.post.PostsResult
import com.uragiristereo.mejiboard.domain.entity.booru.tag.TagsResult

interface BooruRepository {
    var boorus: Map<BooruSource, BooruSourceRepository>

    val currentBooru: BooruSource

    suspend fun getPosts(tags: String, page: Int): PostsResult

    suspend fun searchTerm(term: String): TagsResult

    suspend fun getTags(tags: List<String>): TagsResult
}
