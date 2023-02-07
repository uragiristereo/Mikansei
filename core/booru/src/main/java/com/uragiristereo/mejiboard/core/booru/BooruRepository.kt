package com.uragiristereo.mejiboard.core.booru

import com.uragiristereo.mejiboard.core.model.booru.BooruSource
import com.uragiristereo.mejiboard.core.model.booru.BooruSourceRepository
import com.uragiristereo.mejiboard.core.model.booru.post.PostsResult
import com.uragiristereo.mejiboard.core.model.booru.tag.TagsResult

interface BooruRepository {
    var boorus: Map<BooruSource, BooruSourceRepository>

    val currentBooru: BooruSource

    suspend fun getPosts(tags: String, page: Int): PostsResult

    suspend fun searchTerm(term: String): TagsResult

    suspend fun getTags(tags: List<String>): TagsResult
}
