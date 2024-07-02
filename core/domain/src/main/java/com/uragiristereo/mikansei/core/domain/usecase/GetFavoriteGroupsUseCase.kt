package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.domain.module.database.SessionRepository
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import java.util.UUID

class GetFavoriteGroupsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val getPostsUseCase: GetPostsUseCase,
) {
    suspend operator fun invoke(forceRefresh: Boolean): Result<List<Favorite>> {
        val activeUser = userRepository.active.value

        val result = danbooruRepository.getFavoriteGroups(
            creatorId = activeUser.id,
            forceRefresh = forceRefresh,
        )

        when (result) {
            is Result.Success -> {
                val favoriteGroups = result.data

                val thumbnailPostIds = favoriteGroups.mapNotNull {
                    it.postIds.maxOrNull()
                }

                val sessionId = UUID.randomUUID().toString()
                val separatedIds = thumbnailPostIds.joinToString(separator = ",")

                return getPostsUseCase.invoke(
                    sessionId = sessionId,
                    tags = "id:$separatedIds status:any",
                    page = 1,
                ).mapSuccess { postsResult ->
                    val posts = postsResult.posts

                    favoriteGroups.map { favoriteGroup ->
                        val thumbnailPostId = favoriteGroup.postIds.maxOrNull()

                        val post = posts.firstOrNull {
                            it.id == thumbnailPostId
                        }

                        favoriteGroup.copy(
                            thumbnailUrl = post?.medias?.preview?.url,
                        )
                    }
                }.also {
                    sessionRepository.delete(sessionId)
                }
            }

            else -> return result
        }
    }
}
