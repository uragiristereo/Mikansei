package com.uragiristereo.mikansei.core.domain.usecase

import com.uragiristereo.mikansei.core.danbooru.repository.DanbooruRepository
import com.uragiristereo.mikansei.core.database.dao.user.UserDao
import com.uragiristereo.mikansei.core.database.dao.user.toUser
import com.uragiristereo.mikansei.core.database.dao.user.toUserRow
import com.uragiristereo.mikansei.core.domain.entity.user.UserField
import com.uragiristereo.mikansei.core.domain.entity.user.toDanbooruUserField
import com.uragiristereo.mikansei.core.model.result.Result
import com.uragiristereo.mikansei.core.model.user.isAnonymous
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class UpdateUserSettingsUseCase(
    private val danbooruRepository: DanbooruRepository,
    private val userDao: UserDao,
) {
    suspend operator fun invoke(data: UserField): Flow<Result<Unit>> {
        val activeUser = userDao.getActive().first().toUser()

        return when {
            activeUser.isAnonymous() -> flow {
                val changes = activeUser.copy(
                    safeMode = data.safeMode ?: activeUser.safeMode,
                    showDeletedPosts = data.showDeletedPosts ?: activeUser.showDeletedPosts,
                    defaultImageSize = data.defaultImageSize ?: activeUser.defaultImageSize,
                    blacklistedTags = data.blacklistedTags ?: activeUser.blacklistedTags,
                ).toUserRow()

                userDao.update(changes)

                emit(Result.Success(Unit))
            }

            else -> danbooruRepository.updateUserSettings(
                id = activeUser.id,
                data = data.toDanbooruUserField(),
            )
        }
    }
}
