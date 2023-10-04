package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.model.preferences.user.RatingPreference
import com.uragiristereo.mikansei.core.model.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SyncUserSettingsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userDao: UserDao,
) {
    suspend operator fun invoke(): Flow<Result<Unit>> {
        val activeUser = userDao.getActive().first()
        val isUserAnonymous = activeUser.id == 0

        return when {
            isUserAnonymous -> flow {
                emit(Result.Success(Unit))
            }

            else -> danbooruRepository.getProfile()
                .map { result ->
                    when (result) {
                        is Result.Success -> {
                            val profile = result.data
                            val user = userDao.get(profile.id).first()

                            profile.apply {
                                userDao.update(
                                    user.copy(
                                        name = name,
                                        level = level.id,
                                        safeMode = danbooru.safeMode,
                                        showDeletedPosts = danbooru.showDeletedPosts,
                                        defaultImageSize = danbooru.defaultImageSize.getEnumForDanbooru(),
                                        blacklistedTags = danbooru.blacklistedTags.joinToString("\n"),
                                        postsRatingFilter = when {
                                            danbooru.safeMode -> RatingPreference.GENERAL_ONLY
                                            else -> user.postsRatingFilter
                                        },
                                    )
                                )
                            }

                            Result.Success(Unit)
                        }

                        is Result.Failed -> Result.Failed(result.message)
                        is Result.Error -> Result.Error(result.t)
                    }
                }
        }
    }
}
