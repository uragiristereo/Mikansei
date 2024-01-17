package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess

class GetFavoriteGroupsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(forceCache: Boolean, forceRefresh: Boolean): Result<List<Favorite>> {
        val activeUser = userRepository.active.value

        when (val result = danbooruRepository.getFavoriteGroups(
            creatorId = activeUser.id,
            forceRefresh = forceRefresh,
        )) {
            is Result.Success -> {
                val favoriteGroups = result.data

                val thumbnailPostIds = favoriteGroups.mapNotNull {
                    it.postIds.maxOrNull()
                }

                return danbooruRepository.getPostsByIds(
                    ids = thumbnailPostIds,
                    forceCache = forceCache,
                    forceRefresh = forceRefresh,
                ).mapSuccess { posts ->
                    favoriteGroups.map { favoriteGroup ->
                        val thumbnailPostId = favoriteGroup.postIds.maxOrNull()

                        val post = posts.firstOrNull {
                            it.id == thumbnailPostId
                        }

                        favoriteGroup.copy(
                            thumbnailUrl = post?.medias?.preview?.url,
                        )
                    }
                }
            }

            else -> return result
        }
    }
}
