package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Tag
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Wiki
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.WikiData
import com.uragiristereo.mikansei.core.domain.module.database.SessionRepository
import com.uragiristereo.mikansei.core.model.danbooru.Post
import com.uragiristereo.mikansei.core.model.result.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.UUID

class GetWikiUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val sessionRepository: SessionRepository,
    private val getPostsUseCase: GetPostsUseCase,
) {
    suspend operator fun invoke(tag: String): Result<WikiData> {
        return when (val wikiResult = danbooruRepository.getWiki(tag)) {
            is Result.Success -> {
                val wiki = wikiResult.data

                return when {
                    wiki != null -> withWiki(wiki)
                    else -> withoutWiki(tag)
                }
            }

            is Result.Failed -> Result.Failed(wikiResult.message)
            is Result.Error -> Result.Error(wikiResult.t)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun withWiki(wiki: Wiki): Result<WikiData> {
        return coroutineScope {
            val htmlDeferred = async { danbooruRepository.parseDtextAsHtml(wiki.body) }
            val tagDeferred = async { danbooruRepository.getTags(listOf(wiki.title)) }
            val results = listOf(htmlDeferred, tagDeferred).awaitAll()
            val htmlResult = results[0] as Result<String>
            val tagResult = results[1] as Result<List<Tag>>
            var html: String? = null
            var tagCategory: Tag.Category? = null
            var postCount: Long? = null
            var posts: List<Post>? = null

            when (htmlResult) {
                is Result.Success -> html = htmlResult.data
                is Result.Failed -> return@coroutineScope Result.Failed(htmlResult.message)
                is Result.Error -> return@coroutineScope Result.Error(htmlResult.t)
            }

            when (tagResult) {
                is Result.Success -> {
                    val firstTag = tagResult.data.firstOrNull()

                    if (firstTag != null) {
                        tagCategory = firstTag.category
                        postCount = firstTag.postCount

                        if (postCount > 0L) {
                            val sessionId = UUID.randomUUID().toString()

                            val postsResult = getPostsUseCase.invoke(
                                sessionId = sessionId,
                                tags = wiki.title,
                                page = 1,
                                limit = 10,
                            )

                            sessionRepository.delete(sessionId)

                            when (postsResult) {
                                is Result.Success -> {
                                    posts = postsResult.data.posts
                                }

                                is Result.Failed -> {
                                    return@coroutineScope Result.Failed(postsResult.message)
                                }

                                is Result.Error -> {
                                    return@coroutineScope Result.Error(postsResult.t)
                                }
                            }
                        }
                    } else {
                        tagCategory = Tag.Category.UNKNOWN
                        postCount = 0L
                    }
                }

                is Result.Failed -> return@coroutineScope Result.Failed(tagResult.message)
                is Result.Error -> return@coroutineScope Result.Error(tagResult.t)
            }

            val data = WikiData(
                tag = wiki.title,
                wiki = wiki.copy(body = html),
                tagCategory = tagCategory,
                postCount = postCount,
                posts = posts,
            )

            Result.Success(data)
        }
    }

    private suspend fun withoutWiki(tag: String): Result<WikiData> {
        val tagResult = danbooruRepository.getTags(listOf(tag))
        var tagCategory: Tag.Category? = null
        var postCount: Long? = null
        var posts: List<Post>? = null

        when (tagResult) {
            is Result.Success -> {
                val firstTag = tagResult.data.firstOrNull()

                if (firstTag != null) {
                    tagCategory = firstTag.category
                    postCount = firstTag.postCount

                    if (postCount > 0L) {
                        val sessionId = UUID.randomUUID().toString()

                        val postsResult = getPostsUseCase.invoke(
                            sessionId = sessionId,
                            tags = tag,
                            page = 1,
                            limit = 10,
                        )

                        sessionRepository.delete(sessionId)

                        when (postsResult) {
                            is Result.Success -> posts = postsResult.data.posts
                            is Result.Failed -> return Result.Failed(postsResult.message)
                            is Result.Error -> return Result.Error(postsResult.t)
                        }
                    }
                } else {
                    tagCategory = Tag.Category.UNKNOWN
                    postCount = 0L
                }
            }

            is Result.Failed -> return Result.Failed(tagResult.message)
            is Result.Error -> return Result.Error(tagResult.t)
        }

        val data = WikiData(
            tag = tag,
            wiki = null,
            tagCategory = tagCategory,
            postCount = postCount,
            posts = posts,
        )

        return Result.Success(data)
    }
}
