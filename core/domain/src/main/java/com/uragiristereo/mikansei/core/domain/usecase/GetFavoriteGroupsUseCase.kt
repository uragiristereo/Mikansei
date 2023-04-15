package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.database.dao.user.toUser
import com.uragiristereo.mikansei.core.domain.entity.favorite.Favorite
import com.uragiristereo.mikansei.core.domain.entity.post.toPost
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.result.mapSuccess
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf

@OptIn(FlowPreview::class)
class GetFavoriteGroupsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userDao: UserDao,
) {
    suspend operator fun invoke(): Flow<Result<List<Favorite>>> {
        val activeUser = userDao.getActive().first().toUser()

        return danbooruRepository.getFavoriteGroups(creatorId = activeUser.id)
            .flatMapConcat { result ->
                when (result) {
                    is Result.Success -> {
                        val favoriteGroups = result.data

                        val thumbnailPostIds = favoriteGroups.mapNotNull {
                            it.postIds.maxOrNull()
                        }

                        danbooruRepository.getPostsByIds(thumbnailPostIds).mapSuccess { posts ->
                            favoriteGroups.map { favoriteGroup ->
                                val thumbnailPostId = favoriteGroup.postIds.maxOrNull()

                                val post = posts.firstOrNull {
                                    it.id == thumbnailPostId
                                }

                                Favorite(
                                    id = favoriteGroup.id,
                                    name = favoriteGroup.name.replace(oldChar = '_', newChar = ' '),
                                    thumbnailUrl = post?.toPost()?.previewImage?.url,
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
