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
    suspend operator fun invoke(
        forceRefresh: Boolean,
        forceLoadFromCache: Boolean,
        shouldLoadThumbnails: Boolean,
    ): Result<List<Favorite.Group>> {
        val activeUser = userRepository.active.value

        val result = danbooruRepository.getFavoriteGroups(
            creatorId = activeUser.id,
            forceRefresh = forceRefresh,
            forceLoadFromCache = forceLoadFromCache,
        )

        when (result) {
            is Result.Success -> {
                val favoriteGroups = result.data

                if (!shouldLoadThumbnails) {
                    return result
                }

                val thumbnailPostIds = favoriteGroups.mapNotNull {
                    it.postIds.maxOrNull()
                }

                return danbooruRepository.getPostsByIds(
                    ids = thumbnailPostIds,
                    extraTags = "status:any",
                    forceCache = false,
                    forceRefresh = true,
                ).mapSuccess { postsResult ->
                    favoriteGroups.map { favoriteGroup ->
                        val thumbnailPostId = favoriteGroup.postIds.maxOrNull()

                        val post = postsResult.firstOrNull {
                            it.id == thumbnailPostId
                        }

                        favoriteGroup.copy(
                            thumbnailPost = post,
                        )
                    }
                }
            }

            else -> return result
        }
    }
}
