package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.danbooru.model.user.field.DanbooruUserField
import com.uragiristereo.mikansei.core.danbooru.model.user.field.DanbooruUserFieldData
import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.user.preference.RatingPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class UpdateUserSettingsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userDao: UserDao,
) {

    suspend operator fun invoke(
        data: DanbooruUserFieldData,
    ): Flow<Result<Unit>> {
        val activeUser = userDao.getActive().first()
        val isUserAnonymous = activeUser.id == 0

        return when {
            isUserAnonymous -> flow {
                var changes = activeUser

                data.enableSafeMode?.let {
                    changes = changes.copy(safeMode = it)

                    if (it) {
                        changes = changes.copy(postsRatingFilter = RatingPreference.GENERAL_ONLY)
                    }
                }

                data.showDeletedPosts?.let {
                    changes = changes.copy(showDeletedPosts = it)
                }

                data.defaultImageSize?.let {
                    changes = changes.copy(defaultImageSize = it)
                }

                userDao.update(changes)

                emit(Result.Success(Unit))
            }

            else -> danbooruRepository.updateUserSettings(
                id = activeUser.id,
                data = DanbooruUserField(data),
            ).map { result ->
                when (result) {
                    is Result.Success -> Result.Success(Unit)
                    is Result.Failed -> {
                        Timber.d(result.message)
                        Result.Failed(result.message)
                    }
                    is Result.Error -> {
                        result.t.printStackTrace()
                        Result.Error(result.t)
                    }
                }
            }
        }
    }
}
