package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.Favorite
import com.uragiristereo.mikansei.core.domain.module.database.UserRepository
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf

@OptIn(FlowPreview::class)
class GetFavoriteGroupsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userRepository: UserRepository,
) {
    operator fun invoke(forceCache: Boolean, forceRefresh: Boolean): Flow<Result<List<Favorite>>> {
        val activeUser = userRepository.active.value

        return danbooruRepository.getFavoriteGroups(creatorId = activeUser.id, forceRefresh)
            .flatMapConcat { result ->
                when (result) {
                    is Result.Success -> {
                        val favoriteGroups = result.data

                        val thumbnailPostIds = favoriteGroups.mapNotNull {
                            it.postIds.maxOrNull()
                        }

                        danbooruRepository.getPostsByIds(
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

                    is Result.Failed -> flowOf(Result.Failed(result.message))
                    is Result.Error -> flowOf(Result.Error(result.t))
                }
            }
    }
}
