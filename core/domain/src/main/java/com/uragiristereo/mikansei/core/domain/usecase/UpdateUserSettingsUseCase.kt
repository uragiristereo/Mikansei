package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.domain.module.danbooru.DanbooruRepository
import com.uragiristereo.mikansei.core.domain.module.danbooru.entity.ProfileSettingsField
import com.uragiristereo.mikansei.core.model.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class UpdateUserSettingsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userDao: UserDao,
) {
    suspend operator fun invoke(data: ProfileSettingsField): Flow<Result<Unit>> {
        val activeUser = userDao.getActive().first()

        return when {
            activeUser.id == 0 -> flow {
                val changes = activeUser.copy(
                    safeMode = data.enableSafeMode ?: activeUser.safeMode,
                    showDeletedPosts = data.showDeletedPosts ?: activeUser.showDeletedPosts,
                    defaultImageSize = data.defaultImageSize?.getEnumForDanbooru() ?: activeUser.defaultImageSize,
                    blacklistedTags = data.blacklistedTags?.joinToString("\n") ?: activeUser.blacklistedTags,
                )

                userDao.update(changes)

                emit(Result.Success(Unit))
            }

            else -> danbooruRepository.updateUserSettings(
                id = activeUser.id,
                field = data,
            )
        }
    }
}
